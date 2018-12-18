package mypackage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpsConnection;

import net.rim.device.api.crypto.certificate.CertificateException;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.MainScreen;

public final class MyScreen extends MainScreen
{
	private final String[] servicos = new String[] {"Status Serviço", "Situação NF-e", "Situação Cadastral"};
	private ObjectChoiceField ocfServico = new ObjectChoiceField("Serviço: ", servicos);
	private ButtonField btnConsumirWS = new ButtonField("Consumir WS");
	private BasicEditField edtChave = new BasicEditField("","");
	private BasicEditField edtXMLRetorno = new BasicEditField("", "");

	HttpsConnection conn = null;

	//=========================================================================================//                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
	// Define parametros para consultar o WS de Status do Servico de comunicacao na SEFAZ RS   //                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
	//=========================================================================================//                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
	// URL                                                                                     //                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
	//=========================================================================================//                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
	String urlStatus = new String("https://nfe.sefaz.rs.gov.br/ws/nfeStatusServico/nfeStatusServico2.asmx");

	//Mensagem SOAP que sera' enviada
	String dadosStatus = new String("<?xml version=\"1.0\" encoding=\"utf-8\"?><soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\"><soap12:Header><nfeCabecMsg xmlns=\"http://www.portalfiscal.inf.br/nfe/wsdl/NfeStatusServico2\"><cUF>43</cUF><versaoDados>2.00</versaoDados></nfeCabecMsg></soap12:Header><soap12:Body><nfeDadosMsg xmlns=\"http://www.portalfiscal.inf.br/nfe/wsdl/NfeStatusServico2\"><consStatServ xmlns=\"http://www.portalfiscal.inf.br/nfe\" versao=\"2.00\"><tpAmb>1</tpAmb><cUF>43</cUF><xServ>STATUS</xServ></consStatServ></nfeDadosMsg></soap12:Body></soap12:Envelope>");

	//SoapAction
	static final String urnStatus = "NfeStatusServico2";

	//=========================================================================================//                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
	// Define parametros para consultar o WS de Situacao Cadastral do Contribuinte na SEFAZ RS //                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
	//=========================================================================================//                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
	// URL                                                                                     //                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
	//=========================================================================================//                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        

	//URL
	String urlSitCli = new String("https://sef.sefaz.rs.gov.br/ws/cadconsultacadastro/cadconsultacadastro2.asmx");
	//Mensagem SOAP que sera' enviada
	String dadosSitCli1 = new String("<?xml version=\"1.0\" encoding=\"UTF-8\"?><soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\"><soap12:Header><nfeCabecMsg xmlns=\"http://www.portalfiscal.inf.br/nfe/wsdl/CadConsultaCadastro2\"><cUF>43</cUF><versaoDados>2.00</versaoDados></nfeCabecMsg></soap12:Header><soap12:Body><nfeDadosMsg xmlns=\"http://www.portalfiscal.inf.br/nfe/wsdl/CadConsultaCadastro2\"><ConsCad xmlns=\"http://www.portalfiscal.inf.br/nfe\" versao=\"2.00\"><infCons><xServ>CONS-CAD</xServ><UF>RS</UF><CNPJ>");
	String dadosSitCli2 = new String("</CNPJ></infCons></ConsCad></nfeDadosMsg></soap12:Body></soap12:Envelope>");
	//SoapAction
	static final String urnSitCli = "consultaCadastro2";

	//=========================================================================================//                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
	// Define parametros para consultar o WS de Situacao da NF-e na SEFAZ RS                   //
	//=========================================================================================//                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
	// URL                                                                                     //                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
	//=========================================================================================//                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
	//
	//URL
	String urlSitNFe = new String("https://nfe.sefaz.rs.gov.br/ws/nfeConsulta/nfeConsulta2.asmx");

	//Mensagem SOAP que sera' enviada (primeira parte - antes da chave de acesso, que deve ser informada)
	String dadosSitNFe1 = new String("<?xml version=\"1.0\" encoding=\"utf-8\"?><soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\"><soap12:Header><nfeCabecMsg xmlns=\"http://www.portalfiscal.inf.br/nfe/wsdl/NfeConsulta2\"><cUF>43</cUF><versaoDados>2.00</versaoDados></nfeCabecMsg></soap12:Header><soap12:Body><nfeDadosMsg xmlns=\"http://www.portalfiscal.inf.br/nfe/wsdl/NfeConsulta2\"><consSitNFe xmlns=\"http://www.portalfiscal.inf.br/nfe\" versao=\"2.00\"><tpAmb>1</tpAmb><xServ>CONSULTAR</xServ><chNFe>");

	//Mensagem SOAP que sera' enviada (segunda parte - depois da chave de acesso, que deve ser informada)
	String dadosSitNFe2 = new String("</chNFe></consSitNFe></nfeDadosMsg></soap12:Body></soap12:Envelope>");
	static final String urnSitNFe = "NfeConsulta2";

	static String result = "";
	public String responseContent;

	public MyScreen()
	{        
		// Set the displayed title of the screen       
		setTitle("WebServices Nota Fiscal Eletrônica - SEFAZ/RS");

		add(ocfServico);
		add(edtChave);
		add(btnConsumirWS);
		add(new SeparatorField());
		add(edtXMLRetorno);

		ocfServico.setChangeListener( new FieldChangeListener() {
			public void fieldChanged(Field arg0, int arg1 ) {
				edtChave.setText(null);
				edtXMLRetorno.setText(null);
				switch (ocfServico.getSelectedIndex()) {
				case 0: //Status do servico
					edtChave.setLabel("");
					break;
				case 1: //Situacao da NFe
					edtChave.setLabel("Chave de acesso: ");
					break;
				case 2: //Situacao cadastral do contribuinte
					edtChave.setLabel("CNPJ do contribuinte: ");
					break;
				default:
					break;
				}
			}
		} );

		btnConsumirWS.setChangeListener( new FieldChangeListener() {
			public void fieldChanged(Field arg0, int arg1 ) {
				ConsomeWS();
			}
		} );
	}

	private void ConsomeWS(){
		String urlWS = new String("");
		String dadosWS = new String("");
		String urnWS = new String("");

		edtXMLRetorno.setText(null);

		switch (ocfServico.getSelectedIndex()) {
		case 0: //Status do servico
			urlWS = urlStatus;
			dadosWS = dadosStatus;
			urnWS = urnStatus;
			break;
		case 1: //Situacao da NFe
			urlWS = urlSitNFe;
			dadosWS = dadosSitNFe1 + edtChave.getText().toString() + dadosSitNFe2;
			urnWS = urnSitNFe;
			break;
		case 2: //Situacao cadastral do contribuinte
			urlWS = urlSitCli;
			dadosWS = dadosSitCli1 + edtChave.getText().toString() + dadosSitCli2;
			urnWS = urnSitCli;
			break;
		default:
			break;
		}

		try
		{
			conn = (HttpsConnection)Connector.open(urlWS);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

		try {
			//Define os cabecalhos do HTTP
			String type = "application/soap+xml; charset=utf-8";
			String soapAction = urnWS;

			conn.setRequestProperty("Content-Type", type);
			conn.setRequestProperty("Content-Length", String.valueOf(dadosWS.length()));
			conn.setRequestProperty("SOAPAction", soapAction);

			System.out.println(dadosWS);
			
			OutputStream out = conn.openOutputStream();
			out.write(dadosWS.getBytes());
			conn.setRequestMethod("POST");
			String resp = getResponseContent( conn );

			switch (ocfServico.getSelectedIndex()) {
			case 0: //Status do servico
				NFeStatusServicoRes statusServ = new NFeStatusServicoRes(resp);
				resp = statusServ.ConverteStatusServicoTxt();
				break;
			case 1: //Situacao da NFe
				NFESituacaoNFRes sitNFe = new NFESituacaoNFRes(resp);
				resp = sitNFe.NFESituacaoNFRes();
				break;
			case 2: //Situacao cadastral do contribuinte
				NFeConsultaContribuinteRes consCont = new NFeConsultaContribuinteRes(resp);
				resp = consCont.ConverteCadastroContribuinteTxt();
				break;
			default:
				break;
			}
			
			edtXMLRetorno.setText(resp);
		} catch(Exception e){
			edtXMLRetorno.setText(e.getMessage().toString());
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (IOException e) {
				edtXMLRetorno.setText(e.getMessage().toString());
				e.printStackTrace();
			}
		}
	}       

	private String getResponseContent( HttpsConnection conn ) throws CertificateException, IOException {
		InputStream is = null;
		try{
			is = conn.openInputStream();
			// Get the length and process the data
			int len = (int) conn.getLength();
			if ( len > 0 ) {
				int actual = 0;
				int bytesread = 0;
				byte[] data = new byte[len];
				while ( ( bytesread != len ) && ( actual != -1 ) ) {
					actual = is.read( data, bytesread, len - bytesread );
					bytesread += actual;
				}
				responseContent = new String (data);
				System.out.println("Response="+responseContent);
			} else {
				int ch;
				while ( ( ch = is.read() ) != -1 ) {
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage().toString());
			edtXMLRetorno.setText(e.getMessage().toString());
			e.printStackTrace();
		}
		return responseContent;
	}
}
