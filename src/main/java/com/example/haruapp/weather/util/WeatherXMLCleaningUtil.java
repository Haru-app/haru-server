package com.example.haruapp.weather.util;

import com.example.haruapp.global.error.CustomException;
import com.example.haruapp.global.error.ErrorCode;
import com.example.haruapp.weather.dto.WeatherForecastDto;
import com.example.haruapp.weather.dto.WeatherItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WeatherXMLCleaningUtil {

    public static final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    public static final String BASE_URL =
            "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst" +
                    "?serviceKey=%s&numOfRows=100&pageNo=1&dataType=XML&base_date=%s&base_time=%s&nx=%d&ny=%d";
    public static final Set<String> targetCategories = Set.of("SKY", "PTY", "RN1", "T1H", "REH", "WSD");
    public static final int NX = 60;
    public static final int NY = 127;

    public static Document parseXml(String xml) {
        try {
            DocumentBuilder builder = factory.newDocumentBuilder(); // 여기선 매번 새로 생성
            return builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new CustomException(ErrorCode.PARSE_ERROR);
        }
    }

    public static WeatherForecastDto dataCleaning(NodeList itemList) {

        List<WeatherItem> items = new ArrayList<>();
        String forecastTime = null;

        for (int i = 0; i < itemList.getLength(); i++) {
            Element item = (Element) itemList.item(i);

            String category = getTagValue("category", item);
            String fcstTime = getTagValue("fcstTime", item);

            // 가장 이른 시간 기준으로 하나만 사용
            if (forecastTime == null) forecastTime = fcstTime;
            if (!forecastTime.equals(fcstTime)) continue;
            if (!targetCategories.contains(category)) continue;

            String value = getTagValue("fcstValue", item);

            String label = getLabel(category, value);

            items.add(WeatherItem.of(category, label, value));
        }

        return WeatherForecastDto.of(forecastTime, NX, NY, items);
    }

    public static String getLabel(String category, String value) {
        return switch (category) {

            case "SKY" -> switch (value) {
                case "1" -> "맑음";
                case "3" -> "구름많음";
                case "4" -> "흐림";
                default -> "미정";
            };

            case "PTY" -> switch (value) {
                case "0" -> "비나 눈 없음";
                case "1" -> "비";
                case "2" -> "비/눈";
                case "3" -> "눈";
                case "4" -> "소나기";
                default -> "미정";
            };

            case "RN1" -> "강수량";
            case "T1H" -> "기온(℃)";
            case "REH" -> "습도(%)";
            case "WSD" -> "풍속(m/s)";
            default -> "기타";
        };
    }

    public static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);

        if (nodeList.getLength() == 0)
            return "";

        Node node = nodeList.item(0);

        return node.getTextContent();
    }
}
