package com.efun.googlepay.callback;

import java.util.Vector;


public class EfunWalletService {
	
	private static EfunWalletService walletService = null;
	
	private static Vector<EfunWalletListener> walletListeners = new Vector<EfunWalletListener>();
	
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

	public  Vector<EfunWalletListener> getWalletListeners() {
		return walletListeners;
	}

	public  void addWalletListeners(EfunWalletListener walletListeners) {
		if (walletListeners != null) {
			EfunWalletService.walletListeners.add(walletListeners);
		}
		
	}

	
}
