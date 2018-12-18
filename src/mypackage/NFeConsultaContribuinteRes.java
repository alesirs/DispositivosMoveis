package mypackage;

public class NFeConsultaContribuinteRes {
	private String dhCons = new String();		//Datahora da consulta
	private String IE = new String();			//Inscricao Estadual
	private String UF = new String();			//UF do contribuinte
	private String cSit = new String();			//Codigo da situacao
	private int indCredNFe;						//Contribuinte obrigado a emitir NFe
	private String xNome = new String();		//Nome do contribuinte
	private String xFant = new String();		//Nome fantasia do contribuinte
	private String xLgr = new String();			//Logradouro
	private String nro = new String();			//Numero
	private String xBairro = new String();		//Bairro...
	private String xMun = new String();			//Municipio...
	private String CEP = new String();			//CEP...
    private String respostaXML;					//Resposta fornecida pelo WS da Sefaz

	public NFeConsultaContribuinteRes(String RespostaXML){
		this.respostaXML = RespostaXML;
	}
	
	public String ConverteCadastroContribuinteTxt(){
		String mensagem = new String("");
		String credNFe = new String("");
		mensagem = respostaXML;
		int posIni = mensagem.indexOf("<CNPJ>");
		int posFim = mensagem.indexOf("</infCad>");
		mensagem = mensagem.substring(posIni,posFim);
		dhCons = mensagem.substring(mensagem.indexOf("<dhCons>")+8,mensagem.indexOf("</dhCons>"));
		IE = mensagem.substring(mensagem.indexOf("<IE>")+4,mensagem.indexOf("</IE>"));
		UF = mensagem.substring(mensagem.indexOf("<UF>")+4,mensagem.indexOf("</UF>"));
		cSit = mensagem.substring(mensagem.indexOf("<cSit>")+6,mensagem.indexOf("</cSit>"));
		if (cSit == "0"){
			cSit = "Nao habilitado";
		} else {
			cSit = "Habilitado";
		}
		credNFe = mensagem.substring(mensagem.indexOf("<indCredNFe>")+12,mensagem.indexOf("</indCredNFe>"));
		indCredNFe = Integer.parseInt(credNFe);
		switch (indCredNFe) {
		case 0: credNFe = "Nao credenciado";
			break;
		case 1: credNFe = "Credenciado";
			break;
		case 2: credNFe = "Cred. todas oper";
			break;
		case 3: credNFe = "Credenciado parcial";
			break;
		case 4: credNFe = "SEFAZ nao informa...";
			break;
		default:
			break;
		}

		xNome = mensagem.substring(mensagem.indexOf("<xNome>")+7,mensagem.indexOf("</xNome>"));
		xFant = mensagem.substring(mensagem.indexOf("<xFant>")+7,mensagem.indexOf("</xFant>"));
		xLgr = mensagem.substring(mensagem.indexOf("<xLgr>")+6,mensagem.indexOf("</xLgr>"));
		nro = mensagem.substring(mensagem.indexOf("<nro>")+5,mensagem.indexOf("</nro>"));
		xBairro = mensagem.substring(mensagem.indexOf("<xBairro>")+9,mensagem.indexOf("</xBairro>"));
		xMun = mensagem.substring(mensagem.indexOf("<xMun>")+6,mensagem.indexOf("</xMun>"));
		CEP = mensagem.substring(mensagem.indexOf("<CEP>")+5,mensagem.indexOf("</CEP>"));

		mensagem = "Data da consulta: "+dhCons + "\n" + "I.E.: "+IE + "\n" + "UF: "+UF + "\n" + "Situacao: "+cSit + "\n";
		mensagem = mensagem + "Credenciado a emitir NFe: "+credNFe + "\n" + "Razao Social: "+xNome + "\n" + "Nome Fantasia: "+ xFant + "\n";
		mensagem = mensagem + "Endereco: "+ xLgr + " " + nro + "\n" + "Bairro: "+xBairro + "\n" + "Municipio: "+xMun + "\n" + "CEP: "+CEP;
		
		return mensagem;
	}

}
