package mypackage;

public class NFESituacaoNFRes {
	private String cStat = new String();		//Codigo do status
	private String xMotivo = new String();		//Descricao do motivo
	private String dhRecbto = new String();		//Data hora de recebimento
	private String idProt = new String();		//ID do protocolo de autorizacao
	private String nProt = new String();		//Numero do protocolo
    private String respostaXML;					//Resposta fornecida pelo WS da SEFAZ
 	
	public NFESituacaoNFRes(String RespostaXML){
		this.respostaXML = RespostaXML;
	}

	//Decompoe o XML, separando os campos que interessam
	public String NFESituacaoNFRes(){
		String mensagem = new String("");
		mensagem = respostaXML;
		int posIni = mensagem.indexOf("<cStat>");
		int posFim = mensagem.indexOf("</nfeConsultaNF2Result");
		mensagem = mensagem.substring(posIni,posFim);
		cStat = mensagem.substring(7,mensagem.indexOf("</cStat>"));
		xMotivo = mensagem.substring(mensagem.indexOf("<xMotivo>")+9,mensagem.indexOf("</xMotivo>"));
		
		if (mensagem.indexOf("<infProt Id=") > -1) {
			idProt = mensagem.substring(mensagem.indexOf("<infProt Id=")+12,mensagem.indexOf("><tpAmb>"));
		}
		
		dhRecbto = mensagem.substring(mensagem.indexOf("<dhRecbto>")+10,mensagem.indexOf("</dhRecbto>"));
		
		if (mensagem.indexOf("<nProt>") > -1) {
			nProt = mensagem.substring(mensagem.indexOf("<nProt>")+7,mensagem.indexOf("</nProt>"));
		}
		
		mensagem = "Codigo: "+cStat + "\n" + "Descricao: "+xMotivo + "\n";
		mensagem = mensagem + "id Protocolo: " + idProt + "\n" + "Data: "+dhRecbto + "\n" + "Prot. autor: " + nProt;
		
		return mensagem;
	}

}
