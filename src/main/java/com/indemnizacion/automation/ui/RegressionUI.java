package com.indemnizacion.automation.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.indemnizacion.automation.commons.BaseTest;
import com.indemnizacion.automation.commons.helpers.DriverFacade;

public class RegressionUI extends BaseTest{


    protected DriverFacade webDriverFacade;

    @FindBy(id = "trackLogIn")
    public static WebElement linkLogin;
    @FindBy(className = "perfil")
    public static  WebElement buttonProfile;
    @FindBy(className = "name")
    public static  WebElement labelUsername;

    public static  By outlookNotSignedButton = By.id("trackLogIn");


//     loginPopUpContainer //
    @FindBy(id = "login")
    public static WebElement loginPopUpContainer;
    @FindBy(id = "email")
    public static  WebElement inputUsername;
    @FindBy(id = "password")
    public static  WebElement inputPassword;
    @FindBy(id = "btn_login")
    public static  WebElement buttonLogin;
    
    //simonweb login UI elements//
    @FindBy(xpath="//input[@name='Username']")
	public static WebElement user_id;
	@FindBy(xpath="//input[@type='submit']")
	public static WebElement submit;
	@FindBy(xpath="//input[@id='j_idt138']")
	public static WebElement advertencia;
	@FindBy(xpath="//select[@id='companyForm:companyList']")
	public static WebElement compania;
	@FindBy(xpath="//input[@id='companyForm:j_idt129']")
	public static WebElement compania_continuar;
	@FindBy(xpath="//img[@title='Cargando...']")
	public static WebElement cargando;
	
	
	//simonweb navigation UI elements//
	@FindBy(xpath="//span[text()='Siniestros Test']")
	public static WebElement siniestros_test;
	@FindBy(xpath="//span[text()='Aviso de Siniestros Test']")
	public static WebElement aviso_de_siniestro_test;
	//@FindBy(xpath="//span[text()='Notificacion de Siniestros Test']")
	@FindBy(xpath="//span[text()='Pre-Aviso Indemnizaciones']")
	public static WebElement front_consult_operciones;
	@FindBy(xpath="//td[@id='navigationPath']//span")
	public static WebElement navigation_path;
	
	//ambiente pruebas UI elements//
	@FindBy(xpath="//div[@id='JQuerySlider_home']/ul/li[2]/table/tbody/tr[8]/td[2]")
	public static WebElement ambiente_simonweb;
	@FindBy(xpath="//input[@type='text']")
	public static WebElement usuario;
	@FindBy(xpath="//input[@type='password']")
	public static WebElement contraseña;
	@FindBy(xpath="//input[@type='submit']")
	public static WebElement ingresa;


 

}
