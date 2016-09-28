package com.dotmarketing.business;

import com.dotmarketing.portlets.languagesmanager.model.Language;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LanguageAPITest {

	/*
	 * 1- Put things in cache
	 * 2- Add a language
	 * 3- Make sure cache have all the languages
	 * 4- Remove a language
	 * 5- Make sure cache remove the language
	 * 6- Clear cache
	 * 
	 */
	@Test
	public void languageCache() throws Exception{
		
		CacheLocator.getLanguageCache().putLanguages(APILocator.getLanguageAPI().getLanguages());
		assertEquals(2,CacheLocator.getLanguageCache().getLanguages().size());
		
		Language lan = APILocator.getLanguageAPI().getLanguage(102);
		lan = new Language();
		lan.setCountry("Italy");
		lan.setCountryCode("IT");
		lan.setLanguageCode("it");
		lan.setLanguage("Italian");
		APILocator.getLanguageAPI().saveLanguage(lan);
		
		CacheLocator.getLanguageCache().clearCache();
		CacheLocator.getLanguageCache().putLanguages(APILocator.getLanguageAPI().getLanguages());
		assertEquals(3, CacheLocator.getLanguageCache().getLanguages().size());
		
		APILocator.getLanguageAPI().deleteLanguage(lan);
		
		CacheLocator.getLanguageCache().clearCache();
		CacheLocator.getLanguageCache().putLanguages(APILocator.getLanguageAPI().getLanguages());
		assertEquals(2,CacheLocator.getLanguageCache().getLanguages().size());
		
		
	}
}
