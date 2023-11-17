package com.sigma.affinity;

public class LatestBlockHolder {
	private Long latestBlock;
    private static LatestBlockHolder latestBlockHolderInstance;
	  private LatestBlockHolder() {
		  latestBlock = 0l;
	    }	    
	    public static LatestBlockHolder getInstance() {
	        if(latestBlockHolderInstance == null) {
	            latestBlockHolderInstance = new LatestBlockHolder();
	        }	        
	        return latestBlockHolderInstance;
	    }
		public Long getLatestBlock() {
			return latestBlock;
		}
		public synchronized void setLatestBlock(Long latestBlock) {
			this.latestBlock = latestBlock;
		}
}
