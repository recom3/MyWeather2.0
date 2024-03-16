package com.myweather;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

/**
 * Created by recom3 on 16/03/2024.
 */
public class OpenMeteoParserTest
        //extends TestCase
{
    OpenMeteoParser openMeteoParser = new OpenMeteoParser();

    private static File getFileFromPath(Object obj, String fileName) {
        ClassLoader classLoader = obj.getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        return new File(resource.getPath());
    }

    /**
     * Check this:
     * https://stackoverflow.com/questions/34045574/want-to-run-a-custom-gradle-task-only-when-running-unit-test
     * @throws Exception
     */
    @Test
    public void fileObjectShouldNotBeNull() throws Exception {
        File file = getFileFromPath(this, "res/open_meteo_data.json");
        assertTrue(file != null);

        String fileContent = "";
        FileInputStream fis = new FileInputStream(file);
        try( BufferedReader br =
                     new BufferedReader( new InputStreamReader(fis, "UTF-8")))
        {
            StringBuilder sb = new StringBuilder();
            String line;
            while(( line = br.readLine()) != null ) {
                sb.append( line );
                sb.append( '\n' );
            }
            fileContent = sb.toString();
        }

        OpenMeteoParser.WeatherInfo[] weatherInfo = openMeteoParser.parseHourlyWeather(fileContent);

        System.out.println("deep arr: " + Arrays.deepToString(weatherInfo));
    }
}