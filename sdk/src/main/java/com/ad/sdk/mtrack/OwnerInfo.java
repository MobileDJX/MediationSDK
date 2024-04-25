package com.ad.sdk.mtrack;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;

class OwnerInfo {
               
        private String id 			= "";
        public String email 		= "";
        public String phone 		= "";
        public String accountName 	= "";
        public String name 			= "";

        public OwnerInfo(Context MainActivity) {
                final AccountManager manager = AccountManager.get(MainActivity);
                final Account[] accounts = manager.getAccountsByType("com.google");
                
                if (accounts.length > 0 && accounts[0].name != null) {
                        accountName = accounts[0].name;                                
                        String where=ContactsContract.CommonDataKinds.Email.DATA + " = ?";
                        ArrayList<String> what = new ArrayList<>();
                        what.add(accountName);                        
                        
                        String[] whatarr= what.toArray(new String[0]);
                        ContentResolver cr = MainActivity.getContentResolver();
                        Cursor emailCur = cr.query(
                                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                                        where,
                                        whatarr, null);
                        if (emailCur != null) {
                                while (emailCur.moveToNext()) {

                                        id = emailCur
                                                        .getString(emailCur
                                                                        .getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID));
                                        email = emailCur
                                                        .getString(emailCur
                                                                        .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));


                                        String newName = emailCur
                                                        .getString(emailCur
                                                                        .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                                        if (name == null || newName.length() > name.length())
                                                name = newName;

                                        // get the phone number
                                        Cursor pCur = cr.query(
                                                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                                        null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                                                        + " = ?", new String[] { id }, null);
                                        while (pCur.moveToNext()) {
                                                phone = pCur
                                                                .getString(pCur
                                                                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                        }
                                        pCur.close();

                                }
                        }

                        emailCur.close();
                        
                      
                }
        }
}
