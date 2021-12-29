package com.indemnizacion.automation.ui;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.indemnizacion.automation.commons.BaseTest;
import com.indemnizacion.automation.commons.helpers.DriverFacade;

public class Pre_Aviso_IndemnizacionesUI extends BaseTest{

	protected DriverFacade webDriverFacade;

	
	
	@FindBy(xpath="//td[@id='navigationPath']//span")
	public static WebElement navigation_path;

	@FindBy(xpath="//select[@id='searchForm:insuredDocType']")
	public static WebElement aseg_tipo_doc;

	@FindBy(xpath="//input[@id='searchForm:insuredDocNo']")
	public static WebElement aseg_num_doc;

	@FindBy(xpath="//input[@id='searchForm:claimDateInputDate']")
	public static WebElement fecha_del_siniestro;

	@FindBy(xpath="//input[@id='searchForm:j_idt221']")
	public static WebElement consultar;

	@FindBy(xpath="//img[@title='Cargando...']")
	public static WebElement cargando;

	@FindBy(xpath="//select[@id='searchForm:clv']")
	public static WebElement clv;

	@FindBy(xpath="//div[@id='searchForm:listingPolicyPanelHeader_header']")
	public static WebElement lista_poliza;

	@FindBy(xpath="//tbody[@id='searchForm:policySearchTable:tb']")
	public List<WebElement> polizas;

	@FindBy(xpath="//tbody[@id='searchForm:policySearchTable:tb']")
	public static WebElement poliza_table;

	@FindBy(xpath="//span[@id='searchForm:policySearchTable:dsPolicy']")
	public static WebElement polizas_table_navigation;

	@FindBy(xpath="//a[@id='searchForm:policySearchTable:dsPolicy_ds_f']")
	public static WebElement navigation_first; 
	
	@FindBy(xpath="//tr[@id='searchForm:riskDataTable:0']")
	public static WebElement riesgo;
	
	@FindBy(xpath="//textarea[@id='searchForm:description']")
	public static WebElement sini_description;

	@FindBy(xpath="//select[@id='searchForm:causa']")
	public static WebElement causa;

	@FindBy(xpath="//select[@id='searchForm:consecuencia']")
	public static WebElement consequncia;

	@FindBy(xpath="//input[@id='searchForm:valorPretencion']")
	public static WebElement valo_pretension;

	@FindBy(xpath="//select[@id='searchForm:cobertura']")
	public static WebElement cobertura;

	@FindBy(xpath="//input[@id='searchForm:next']")
	public static WebElement continuar;

	@FindBy(xpath="//input[@id='searchForm:reset']")
	public static WebElement reset;
	
	@FindBy(xpath="//select[@id='searchForm:causa']")
	public static WebElement causas;

	@FindBy(xpath="//select[@id='searchForm:consecuencia']")
	public static WebElement consequncias;

	@FindBy(xpath="//select[@id='searchForm:cobertura']")
	public static WebElement coberturas;

	@FindBy(xpath="//div[@id='searchForm:j_idt375_body']")
	public static WebElement modelos_panel;

	@FindBy(xpath="//select[@id='searchForm:modeloR']")
	public static WebElement cliente_riesgo;

	@FindBy(xpath="//select[@id='searchForm:modeloRiesgo']")
	public static WebElement modelo_riesgo;

	@FindBy(xpath="//select[@id='searchForm:resultadoEncuesta']")
	public static WebElement resultado_encuesta;

	@FindBy(xpath="//select[@id='searchForm:resultadoAnalysisEvidencia']")
	public static WebElement resultado_evidencia;

	@FindBy(xpath="//select[@id='searchForm:modeloClasificacion']")
	public static WebElement clasificacion;

	@FindBy(xpath="//select[@id='searchForm:resultadoDefinicionMotor']")
	public static WebElement definicion;

	@FindBy(xpath="//textarea[@id='searchForm:motivoCambioDecision']")
	public static WebElement motivo_cambio_decision;

	@FindBy(xpath="//div[@id='footerDiv']//td[@class='mini-pata']")
	public static WebElement page_footer;

	/*@FindBy(xpath="//div[text()='NetIQ Access Manager']")
	public static WebElement session_expire;*/
	
	public static By session_expire=By.xpath("//div[text()='NetIQ Access Manager']"); 
	
	

}
