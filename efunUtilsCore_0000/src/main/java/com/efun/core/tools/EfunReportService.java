package com.efun.core.tools;

import java.util.Vector;

public class EfunReportService {
private static EfunReportService walletService = null;
	
	private static Vector<EfunReportListener> walletListeners = new Vector<EfunReportListener>();
	
	public static EfunReportService getInstance(){
		if (walletService == null) {
			synchronized (EfunReportService.class) {
				if (walletService == null) {
					walletService = new EfunReportService();
				}
			}
		}
		return walletService;
	}

	public  Vector<EfunReportListener> getWalletListeners() {
		return walletListeners;
	}

	public  void addWalletListeners(EfunReportListener walletListeners) {
		if (walletListeners != null) {
			EfunReportService.walletListeners.add(walletListeners);
		}
		
	}
}
