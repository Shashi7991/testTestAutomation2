package com.isa.block.impl;

import com.isa.block.Block;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
@Log4j2
public class OtpBlock implements Block {
    @Autowired
    private WebDriver driver;
    @Autowired
    private WebDriverWait wait;

    private String url = "jdbc:oracle:thin:@10.30.4.38:1521:ISASHJDB";
    @Value("${test.webdriver.database.username}")
    private String username;
    @Value("${test.webdriver.database.password}")
    private String password;
    private String sqlQuery = "SELECT * FROM (SELECT * FROM T_USER_OTP ORDER BY CREATED_DATE DESC) WHERE ROWNUM = 1";

    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    @Override
    public void process(){
        driver.switchTo().parentFrame();
        driver.switchTo().frame(1);
        if(wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/table/tbody/tr/td[2]/table/tbody/tr/td[2]/div/table/tbody/tr[3]/td/div/table/tbody/tr[2]/td[2]/form[2]/table/tbody/tr[6]/td/input[1]"))).isDisplayed()){
            otpFullflow();
            System.out.println("OTP is displayed");
        }
    }

    @SneakyThrows
    private void otpFullflow() {

        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            // 3. Execute the SQL query
            resultSet = statement.executeQuery(sqlQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        while (resultSet.next()) {
            String otp = resultSet.getString("OTP");
            log.info("otp is : " + otp);
            WebElement otpField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/table/tbody/tr/td[2]/table/tbody/tr/td[2]/div/table/tbody/tr[3]/td/div/table/tbody/tr[2]/td[2]/form[2]/table/tbody/tr[3]/td/input")));
            otpField.sendKeys(otp);
            WebElement validateBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/table/tbody/tr/td[2]/table/tbody/tr/td[2]/div/table/tbody/tr[3]/td/div/table/tbody/tr[2]/td[2]/form[2]/table/tbody/tr[6]/td/input[2]")));
            validateBtn.click();
        }

        if (resultSet != null) resultSet.close();
        if (statement != null) statement.close();
        if (connection != null) connection.close();
    }

}
