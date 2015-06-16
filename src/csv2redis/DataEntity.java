package csv2redis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author Muhammad
 */
public class DataEntity {

    private ArrayList<String> keys = null; // CSV Fields
//    private ArrayList<HashMap<String, String>> document; // Single Document
    private ArrayList<ArrayList<LinkedHashMap<String, String>>> dataset = null; // Documents

    public void addKey(String key) {
        if (this.keys == null) {
            this.keys = new ArrayList<>();
        }
        this.keys.add(key);
    }

    public ArrayList<String> getKeys() {
        return this.keys;
    }
    
    public void addDocument(ArrayList<LinkedHashMap<String, String>> document) {
        if (this.dataset == null) {
            dataset = new ArrayList<>();
        }
        this.dataset.add(document);
    }

    public ArrayList<ArrayList<LinkedHashMap<String, String>>> getDocuments() {
        return this.dataset;
    }

    public static String getJsonFormattedStringOfDocument(LinkedHashMap<String, String> document) {
        String str = "{";
        for (String key : document.keySet()) {
            str += "\"" + key + "\":" + "" + document.get(key) + ", ";
        }
        // remove last comma
        int index = str.lastIndexOf(",");
        str = new StringBuilder(str).replace(index, index + 1, "").toString();

        str += "}";
        return str;
    }

    public String getJsonFormattedStringOfDataSet() {
        String str = "";
        for (ArrayList<LinkedHashMap<String, String>> documents : this.getDocuments()) {
            for (LinkedHashMap<String, String> document : documents) {
                str += getJsonFormattedStringOfDocument(document);
            }
        }
        return str;
    }

    static DataEntity getDataEntityFromCSV(String CsvFile) {
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        String csvSplitterIgnoreCommasInQuotes = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";

        DataEntity dataEntity = new DataEntity();
        ArrayList<LinkedHashMap<String, String>> document = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(CsvFile));
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] coloumns = line.split(csvSplitterIgnoreCommasInQuotes);

                if (dataEntity.getKeys() == null) {
                    for (String key : coloumns) {
                        dataEntity.addKey(key);
                    }
                } else {
                    if (coloumns.length < dataEntity.getKeys().size()) {
                        ArrayList<String> tmpColoumns = new ArrayList<>();
                        for (String col : coloumns) {
                            tmpColoumns.add(col);
                        }
                        for (int i = 0; i < dataEntity.getKeys().size() - coloumns.length; i++) {
                            tmpColoumns.add("");
                        }
                        coloumns = tmpColoumns.toArray(coloumns);
                    }
                    LinkedHashMap<String, String> row = new LinkedHashMap<>();
                    for (int i = 0; i < dataEntity.getKeys().size(); i++) {
                        row.put(dataEntity.getKeys().get(i), coloumns[i]);
                    }
                    document.add(row);
                }
            }
            dataEntity.addDocument(document);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return dataEntity;
        }
    }
}
