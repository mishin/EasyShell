/*******************************************************************************
 * Copyright (c) 2014 - 2016 Andre Bossert.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andre Bossert - initial API and implementation and/or initial documentation
 *******************************************************************************/

package de.anbos.eclipse.easyshell.plugin.preferences;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.Constants;

public class MenuDataStore extends DataStore<MenuData> {

    private static MenuDataStore instance = null;

    public static MenuDataStore instance() {
        if (instance == null) {
            instance = new MenuDataStore(Activator.getDefault().getPreferenceStore());
        }
        return instance;
    }

    public MenuDataStore(IPreferenceStore store) {
        super(store);
        store.addPropertyChangeListener(new IPropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                if (event.getProperty().equals(Constants.PREF_MENU)) {
                    load((String)event.getNewValue());
                }
            }
        });
    }

    public MenuData[] getCommandMenuDataArray() {
        List<MenuData> allItems = getDataList();
        if(allItems.size() <= 0) {
            return new MenuData[0];
        }
        MenuData[] allArray = new MenuData[allItems.size()];
        for(int i = 0 ; i < allArray.length ; i++) {
            allArray[i] = allItems.get(i);
        }
        return allArray;
    }

    public List<MenuData> getEnabledCommandMenuDataList() {
        List<MenuData> checkedItems = new ArrayList<MenuData>();
        Iterator<MenuData> dataIterator = getDataList().iterator();
        while(dataIterator.hasNext()) {
            MenuData data = (MenuData)dataIterator.next();
            if(data.isEnabled()) {
                checkedItems.add(data);
            }
        }
        return checkedItems;
    }

    public MenuData[] getEnabledCommandMenuDataArray() {
        List<MenuData> checkedItems = getEnabledCommandMenuDataList();
        if(checkedItems.size() <= 0) {
        	return new MenuData[0];
        }
        MenuData[] checked = new MenuData[checkedItems.size()];
        for(int i = 0 ; i < checked.length ; i++) {
            checked[i] = (MenuData)checkedItems.get(i);
        }
        return checked;
    }

    @Override
    public void save() {
        getStore().setValue(Constants.PREF_MENU,PreferenceValueConverter.asMenuDataString(getDataList()));
    }

    @Override
    public void loadDefaults() {
        getStore().setToDefault(Constants.PREF_MENU);
        load();
    }

    public void load(String prefMenu) {
        if (prefMenu == null) {
            prefMenu = getStore().getString(Constants.PREF_MENU);
        }
        MenuData[] items = PreferenceValueConverter.asMenuDataArray(prefMenu);
        removeAll();
        for(int i = 0 ; i < items.length ; i++) {
            addItem(items[i]);
        }
        sort();
    }

    @Override
    public void load() {
        load(null);
    }

    public List<MenuData> getRefencedBy(String id) {
        List<MenuData> ref = new ArrayList<MenuData>();
        Iterator<MenuData> dataIterator = getDataList().iterator();
        while(dataIterator.hasNext()) {
            MenuData data = (MenuData)dataIterator.next();
            if(data.getCommandId().equals(id)) {
                ref.add(data);
            }
        }
        return ref;
    }

}
