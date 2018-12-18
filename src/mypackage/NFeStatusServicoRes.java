package mypackage;

public class NFeStatusServicoRes {
	private String cStat = new String();	//Codigo do status
	private String xMotivo = new String();	//Descricao do status
	private String dataRec = new String();	//Data de recebimento
	private String tMed = new String();		//Tempo medio de processamento do sistema
    private String respostaXML;				//Resposta fornecida pelo WS da Sefaz
	
	public NFeStatusServicoRes(String RespostaXML){
		this.respostaXML = RespostaXML;
	}
	
	public String ConverteStatusServicoTxt(){
		String mensagem = new String("");
		mensagem = respostaXML;
		int posIni = mensagem.indexOf("<cStat>");
		int posFim = mensagem.indexOf("</tMed>")+7;
		mensagem = mensagem.substring(posIni,posFim);
		cStat = mensagem.substring(7,mensagem.indexOf("</cStat>"));
		xMotivo = mensagem.substring(mensagem.indexOf("<xMotivo>")+9,mensagem.indexOf("</xMotivo>"));
		dataRec = mensagem.substring(mensagem.indexOf("<dhRecbto>")+10,mensagem.indexOf("</dhRecbto>"));
		tMed  = mensagem.substring(mensagem.indexOf("<tMed>")+6,mensagem.indexOf("</tMed>"));

		return "Codigo: "+cStat + "\n" + "Descricao: "+xMotivo + "\n" + "Data: "+dataRec + "\n" + "Tempo Medio de Resposta: "+tMed+" segundos";
	}
}
