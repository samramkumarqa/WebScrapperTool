package testCases;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import Pages.searchpage;

public class ScriptDriver {
	WebDriver driver;
	ExtentTest test;
	ExtentReports report;
	ArrayList<String> primarylinks = new ArrayList<String>();
    ArrayList<String> secondrylinks = new ArrayList<String>();
    ArrayList<String> thirdlinks = new ArrayList<String>();
    ArrayList<String> fourlinks = new ArrayList<String>();
    ArrayList<String> otherlinks = new ArrayList<String>();
    
    List<Object> getprimarylinks = new ArrayList<Object>();
    List<Object> getsecondrylinks = new ArrayList<Object>();
    List<Object> getthirdlinks = new ArrayList<Object>();
    List<Object> getfourlinks = new ArrayList<Object>();
    List<Object> getotherlinks = new ArrayList<Object>();
    
    List<Object> newprimarylinks = new ArrayList<Object>();
    List<Object> newsecondrylinks = new ArrayList<Object>();
    List<Object> newthirdlinks = new ArrayList<Object>();
    List<Object> newfourlinks = new ArrayList<Object>();
    List<Object> newotherlinks = new ArrayList<Object>();

	@Test(dataProvider = "ToolData",threadPoolSize=2)
	public void driverSetup(HashMap<String, String> data) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		try {
			
		report = new ExtentReports("//Users//ramkumars//eclipse-workspace//Selenium_Framework2//src//Results//Amazonpurchaseresults.html");
		test = report.startTest("ExtentDemo");
		
		System.out.print("Got the data value :"+data.get("ToRun"));
		//Firefox driver 123
		/* 
		WebDriver driver = new FirefoxDriver();
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.get("http://google.com");
		driver.quit();*/
		
		//Chrome driver commit check
		//System.setProperty("webdriver.chrome.driver", "/Users/ramkumars/Selenium_Essentials/drivers/chromedriver");
		System.setProperty("webdriver.chrome.driver", "/Users/ramkumars/eclipse-workspace/Selenium_Framework2/src/test/java/Alldrivers/chromedriver");
		
		
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		driver = new ChromeDriver(options);
		
		//driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.get(data.get("URL"));
		java.util.List<WebElement> links = driver.findElements(By.tagName("a"));
	      System.out.println("Number of Links in the Page is " + links.size());
	      
	      
	      for (int i = 1; i<=links.size()-1; i = i+1) {
	         
	         String collectlinks = links.get(i).getAttribute("href");
	         //System.out.println("Name of Link# " + i + links.get(i).getAttribute("href"));
	         if (collectlinks != null && !collectlinks.isEmpty() && collectlinks.contains(data.get("Domain"))) {
	        	 String getlinks="";
	        	 if(collectlinks.contains("?")) {
	        		 //System.out.println(i+" "+collectlinks.substring(0, collectlinks.indexOf("?"))); 
	        		 getlinks = collectlinks.substring(0, collectlinks.indexOf("?"));
	        	 }else if(collectlinks.contains("/ref=")){
	        		//System.out.println(i+" "+collectlinks.substring(0, collectlinks.indexOf("ref"))); 
	        		 getlinks = collectlinks.substring(0, collectlinks.indexOf("/ref="));
	        	 }else {
	        		//System.out.println(i+" "+collectlinks); 
	        		 getlinks = collectlinks;
	        	 }
	        	 
	        	 //Separate links as primary secondry and third
	        	 int count = StringUtils.countMatches(getlinks, "/");
	        	 //System.out.println("Counts : "+count);
	        	 //System.out.println("Get Links"+getlinks);
	        	 if(count==3) {
	        		 primarylinks.add(getlinks);	        		  
                 }else if(count==4) {
	        		 secondrylinks.add(getlinks);
	        	 }else if(count==5) {
	        		 thirdlinks.add(getlinks);
	        	 }else if(count==6) {
	        		 fourlinks.add(getlinks);
	        	 }else if(count>6) {
	        		 otherlinks.add(getlinks);
	        	 }
	        }
	       
	      }
	      searchpage hp = new searchpage(driver, data, primarylinks, secondrylinks, thirdlinks, fourlinks, otherlinks);
	      //Pass primary links and collect values in array
	      getprimarylinks = primarylinks.stream().distinct().collect(Collectors.toList());
	      for (int i = 1; i<=getprimarylinks.size()-1; i = i+1) {
	    	  //System.out.println("Test Here : "+primarylinks.get(i).toString());
	    	  String passthelinks = getprimarylinks.get(i).toString();
	    	  hp.spider(passthelinks);
	      }
	      
	      //Pass secondry links and collect values in array
	      getsecondrylinks = secondrylinks.stream().distinct().collect(Collectors.toList());
	      for (int i = 1; i<=getsecondrylinks.size()-1; i = i+1) {
	    	  //System.out.println("Test Here : "+primarylinks.get(i).toString());
	    	  String passthelinks = getsecondrylinks.get(i).toString();
	    	  hp.spider(passthelinks);
	      }
	      
	      newprimarylinks = primarylinks.stream().distinct().collect(Collectors.toList());
	      newsecondrylinks = secondrylinks.stream().distinct().collect(Collectors.toList());
	      newthirdlinks = thirdlinks.stream().distinct().collect(Collectors.toList());
	      newfourlinks = fourlinks.stream().distinct().collect(Collectors.toList());
	      newotherlinks = otherlinks.stream().distinct().collect(Collectors.toList());
	      
	      
	      hp.writeResults(newprimarylinks, "/Users/ramkumars/eclipse-workspace/WebScrapper/src/test/java/TestData/ToolsQATestData3.xls", "Primarylinks");
	      hp.writeResults(newsecondrylinks, "/Users/ramkumars/eclipse-workspace/WebScrapper/src/test/java/TestData/ToolsQATestData3.xls", "Secondrylinks");
	      hp.writeResults(newthirdlinks, "/Users/ramkumars/eclipse-workspace/WebScrapper/src/test/java/TestData/ToolsQATestData3.xls", "Thirdlinks");
	      hp.writeResults(newfourlinks, "/Users/ramkumars/eclipse-workspace/WebScrapper/src/test/java/TestData/ToolsQATestData3.xls", "Fourthlinks");
	      hp.writeResults(newotherlinks, "/Users/ramkumars/eclipse-workspace/WebScrapper/src/test/java/TestData/ToolsQATestData3.xls", "Otherlinks");
	      
	      
	      System.out.println("Primary Links : "+newprimarylinks);
	      System.out.println("Secondry Links : "+newsecondrylinks);
	      System.out.println("Third Links : "+newthirdlinks);
	      System.out.println("Fourth Links : "+newfourlinks);
	      System.out.println("Other Links : "+newotherlinks);
	      
		
		Thread.sleep(2000);
		
		report.endTest(test);
		report.flush();
		driver.quit();
		}catch(Exception e) {
			e.printStackTrace();
		}

}
	
	@DataProvider(name="ToolData", parallel=false)
	public Iterator<Object[]> getExcelData() throws IOException{
		@SuppressWarnings("rawtypes")
		ArrayList<HashMap> excelData;
		TestDataReader.readExcelFile objExcelFile = new TestDataReader.readExcelFile();
		//excelData = objExcelFile.readExcel("E:\\ExcelData","ToolsQATestData.xls","Sheet1");
		excelData = objExcelFile.readExcel("/Users/ramkumars/eclipse-workspace/WebScrapper/src/test/java/TestData","ToolsQATestData3.xls","Sheet1");
		
		List<Object[]> dataArray = new ArrayList<Object[]>();
		for(HashMap data : excelData){
			dataArray.add(new Object[] { data });
			}
		return dataArray.iterator();
	}

}

