package com.starpy.pay.gp.callback;

import java.util.Vector;


public class EfunWalletService {
	
	private static EfunWalletService walletService = null;
	
	private static Vector<ISWalletListener> walletListeners = new Vector<ISWalletListener>();
	
	private EfunWalletService(){}
	
	public static EfunWalletService getInstance(){
		if (walletService == null) {
			synchronized (EfunWalletService.class) {
				if (walletService == null) {
					walletService = new EfunWalletService();
				}
			}
		}
		return walletService;
	}

	public  Vector<ISWalletListener> getWalletListeners() {
		return walletListeners;
	}

	public  void addWalletListeners(ISWalletListener walletListeners) {
		if (walletListeners != null) {
			EfunWalletService.walletListeners.add(walletListeners);
		}
		
	}

	
}
