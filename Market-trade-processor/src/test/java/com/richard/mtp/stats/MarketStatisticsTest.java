package com.richard.mtp.stats;

import static org.junit.Assert.*;

import org.junit.Test;

public class MarketStatisticsTest {

    @Test
    public void addToAmountOfCurrencySold() {
        MarketStatistics marketStatistics = new MarketStatistics();
    
        String currency = "EUR";
        long amount = 10000;
        
        marketStatistics.add(currency, amount);
        
        assertEquals(amount, marketStatistics.getAmountForCurrency(currency));
    }
    
    @Test
    public void addTwoAmountsOfCurrencySold() {
        MarketStatistics marketStatistics = new MarketStatistics();
    
        String currency = "EUR";
        long amount = 10000;
        
        marketStatistics.add(currency, amount);
        marketStatistics.add(currency, amount);
        
        assertEquals(amount * 2, marketStatistics.getAmountForCurrency(currency));
    }
    
    @Test
    public void addOtherCurrency() {
        MarketStatistics marketStatistics = new MarketStatistics();
        
        String currency = "XXX";
        long amount = 10000;
        
        marketStatistics.add(currency, amount);
        
        assertEquals(amount, marketStatistics.getAmountForCurrency("YYYY"));
    }
    
    @Test
    public void addOtherCurrencyAndAnotherCurrency() {
        MarketStatistics marketStatistics = new MarketStatistics();
        
        String currency = "XXX";
        long amount = 10000;
        
        marketStatistics.add(currency, amount);
        
        currency = "USD";
        amount = 2000;
        
        marketStatistics.add(currency, amount);
        
        assertEquals(amount, marketStatistics.getAmountForCurrency(currency));
        assertEquals(10000, marketStatistics.getAmountForCurrency("XXX"));
    }
    
    @Test
    public void isNoOfTransactionsZeroAtInitialisation() {
    	MarketStatistics marketStatistics = new MarketStatistics();
    	
    	assertEquals(0, marketStatistics.getNoOfTransactions());
    }
    
    @Test
    public void checkNoOftransactionsIsEqualToWhatIsSet() {
    	MarketStatistics marketStatistics = new MarketStatistics();
    	
    	marketStatistics.setNoOfTransactions(123456);
    	
    	assertEquals(123456, marketStatistics.getNoOfTransactions());
    }
    
}
