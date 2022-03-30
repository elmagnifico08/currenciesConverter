package com.example.demo5;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.ResourceBundle;


public class HelloController {
    static  ObjectMapper mapper = new ObjectMapper();
    static String json;

    static {
        try {
            json = getUrlContent("https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static  Map<String, String> currencies;

    static {
        try {
            currencies = mapper.readValue(json, new TypeReference<>() {

            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField amount;

    @FXML
    private Button getCurse;

    @FXML
    private ComboBox<String> baseCurr;

    @FXML
    private ComboBox<String> currTar;


    @FXML
    private Text currenceResult;

    @FXML
    void initialize() throws IOException {

        amount.textProperty().addListener((ov, oldValue, newValue) -> {

            try {
                Integer.parseInt(newValue);
            }
            catch (NumberFormatException e){
                amount.setText(oldValue);
                System.out.println("error");

                }


        });

        String[] arr = currencies.values().toArray(new String[0]);
        ObservableList<String> val =
                FXCollections.observableArrayList(arr);

        baseCurr.setItems(val);
        baseCurr.setValue(val.get(1));

        currTar.setItems(val);
        currTar.setValue(val.get(1));

        amount.setOnAction(ActionEvent -> {
            outResult();
        });

        baseCurr.setOnAction(ActionEvent -> {
            outResult();
        });

        currTar.setOnAction(ActionEvent -> {
            outResult();
        });

        return ;
    }

private void outResult(){
    String baseCurrency = String.valueOf(baseCurr.getValue());
    String targetCurrency = String.valueOf(currTar.getValue());
    String baseIso = null, targetIso = null;

    for (Map.Entry<String, String> entry : currencies.entrySet()) {
        if (baseCurrency.equals(entry.getValue())) {
            baseIso = entry.getKey();
        }
        if (targetCurrency.equals(entry.getValue())) {
            targetIso = entry.getKey();
        }

    }

    Integer getAmount = Integer.valueOf(amount.getText());
    double result = (double)getAmount;

    String output = null;
    try {
        output = getUrlContent("https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/" + baseIso + "/" + targetIso + ".json");
    } catch (IOException e) {
        e.printStackTrace();
    }
    JsonNode jsonNode = null;
    try {
        jsonNode = mapper.readTree(output);
    } catch (JsonProcessingException e) {
        e.printStackTrace();
    }
    Double getDouble = Double.valueOf(String.valueOf(jsonNode.get(targetIso).asDouble()));
    double out = getDouble * getAmount;
    currenceResult.setText(String.valueOf(out));
}
    private static String getUrlContent(String urlAdress) throws IOException {
        StringBuffer content = new StringBuffer();

        try {
            URL url = new URL(urlAdress);
            URLConnection urlConn = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String line;

            while((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch(Exception e) {
            System.out.println("no currency");
        }
        return content.toString();
    }


}