import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeInsideCodeControl {

    private static String Appkey = "646070804302357494BC7B7B2EC32095";
    private static String AppSercet = "b393064516524b91baab90c36eaebd6f";
    private static String AccessToken = "7a4034cd-4eb5-4d3e-946c-a63d04210a6d";

    private List<DealItem> data;


    public void run() throws IOException, InvalidFormatException {
        getData();
        List<DealItem> dealItems = new ArrayList<DealItem>();
        int count = 0;
        for (DealItem item : data) {
            dealItems.add(item);
            count++;
            if (count == 5) {
                ChangeInsideCode change = new ChangeInsideCode(Appkey, AppSercet, AccessToken);
                change.setItems(dealItems);
                change.deal();
                count = 0;
                dealItems.clear();
            }
        }
    }

    public void runOne() throws IOException, InvalidFormatException {
        getData();
        List<DealItem> dealItems = new ArrayList<DealItem>();
        dealItems.add(data.get(0));

        ChangeInsideCode change = new ChangeInsideCode(Appkey, AppSercet, AccessToken);
        change.setItems(dealItems);
        change.deal();

    }

    public void getData() throws IOException, InvalidFormatException {
        ReadExcel readExcel = new ReadExcel();
        try {


            XSSFSheet sheet = readExcel.readExcel("/Users/ming/desktop/test.xlsx");

            Map<String, DealItem> items = new HashMap<String, DealItem>();
            int rowstart = sheet.getFirstRowNum();
            int rowEnd = sheet.getLastRowNum();

            Map<String, Map<String, String>> skumap = new HashMap<String, Map<String, String>>();
            for (int i = rowstart+1; i <= rowEnd; i++) {

                XSSFRow row = sheet.getRow(i);
                if (null == row) continue;
                DealItem item = new DealItem();
                int cellStart = row.getFirstCellNum();
                int cellEnd = row.getLastCellNum();
                item.setShopName(row.getCell(0).getStringCellValue());
                item.setNumId(row.getCell(1).getStringCellValue());
                if (skumap.containsKey(item.getNumId())) {
                    Map<String, String> map = skumap.get(item.getNumId());
                    if (!map.containsKey(row.getCell(3).getStringCellValue())) {
                        map.put(row.getCell(3).getStringCellValue(), row.getCell(5).getStringCellValue());
                    }
                } else {
                    Map<String, String> map = new HashMap<String, String>();
                    skumap.put(item.getNumId(), map);
                    map.put(row.getCell(3).getStringCellValue(), row.getCell(5).getStringCellValue());
                }
                if (!items.containsKey(item.getNumId())) {
                    items.put(item.getNumId(), item);
                }
            }
            List<DealItem> result = new ArrayList<DealItem>();
            for (Map.Entry<String, DealItem> i : items.entrySet()) {
                i.getValue().setInsideDict(skumap.get(i.getKey()));
                result.add(i.getValue());
            }
            data = result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}

    
    
    
    
    
    
    
    
    
    