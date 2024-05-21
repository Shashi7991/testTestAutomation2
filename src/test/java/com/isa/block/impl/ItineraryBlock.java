package com.isa.block.impl;

import com.isa.block.Block;
import com.isa.constant.ExcelColumns;
import com.isa.utility.ExcelUtility;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Log4j2
public class ItineraryBlock implements Block {
    @Autowired
    private WebDriverWait wait;
    @Autowired
    private ExcelUtility excelUtility;
    public static String pnrNo;

    @Override
    public void process() {
        goToReservationPage();
    }

    private void goToReservationPage() {
        WebElement pnrLink = wait.until(ExpectedConditions.elementToBeClickable(By.id("itineraryPNRLink")));
        log.info("Created PNR: {}", pnrLink.getText());

        List<String> flightDetails = new ArrayList<>();
        List<WebElement> flightDetailsRows = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("#itnFlightDetails > tr")));
        for (WebElement row : flightDetailsRows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));

            for (WebElement cell : cells) {
                List<WebElement> paragraphs = cell.findElements(By.tagName("p"));

                for (WebElement paragraph : paragraphs) {
                    if (!paragraph.getText().isBlank()) {
                        flightDetails.add(paragraph.getText());
                    }
                }
            }
        }
        log.info("PNR Flight Details: {}", flightDetails);

        List<String> paxDetails = new ArrayList<>();
        List<WebElement> paxDetailsRows = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("#itnPaxDet > tr")));
        for (WebElement row : paxDetailsRows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));

            for (WebElement cell : cells) {
                List<WebElement> paragraphs = cell.findElements(By.tagName("p"));

                for (WebElement paragraph : paragraphs) {
                    if (!paragraph.getText().isBlank()) {
                        paxDetails.add(paragraph.getText());
                    }
                }
            }
        }
        log.info("PNR Pax Details: {}", paxDetails);

        List<String> priceDetails = new ArrayList<>();
        List<WebElement> priceDetailsRows = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("#itnPriceDetails > tr")));
        for (WebElement row : priceDetailsRows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));

            for (WebElement cell : cells) {
                List<WebElement> paragraphs = cell.findElements(By.cssSelector("label, p"));

                for (WebElement paragraph : paragraphs) {
                    if (!paragraph.getText().isBlank()) {
                        priceDetails.add(paragraph.getText());
                    }
                }
            }
        }

        List<WebElement> trITNDiscountRows = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("#itnPriceDetails + tfoot > tr")));
        for (WebElement row : trITNDiscountRows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));

            for (WebElement cell : cells) {
                List<WebElement> paragraphs = cell.findElements(By.cssSelector("label, div"));

                for (WebElement paragraph : paragraphs) {
                    if (!paragraph.getText().isBlank()) {
                        priceDetails.add(paragraph.getText());
                    }
                }
            }
        }
        log.info("PNR Price Details: {}", priceDetails);

        excelUtility.writeToNewExcelCell(ExcelColumns.PNR_COLUMN_INDEX, pnrLink.getText());
        excelUtility.writeToNewExcelCell(ExcelColumns.FLIGHT_DETAILS_COLUMN_INDEX, String.join(" ", flightDetails));
        excelUtility.writeToNewExcelCell(ExcelColumns.PAX_DETAILS_COLUMN_INDEX, String.join(" ", paxDetails));
        excelUtility.writeToNewExcelCell(ExcelColumns.PRICE_DETAILS_COLUMN_INDEX, String.join(" ", priceDetails));
        pnrLink.click();
    }

}
