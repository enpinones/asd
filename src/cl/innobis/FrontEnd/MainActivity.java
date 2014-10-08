package cl.innobis.frontend;

import innobis.kanban.cl.R;

import java.util.ArrayList;

import cl.innobis.adapters.*;
import cl.innobis.fragments.*;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends Activity {

	//Variables del menu lateral
	private DrawerLayout lateralMenuDrawerLayout;
	private ListView lateralMenuListView;
	private ActionBarDrawerToggle lateralMenuDrawerToggle;
	private CharSequence lateralMenuTitle;
	//Titulos e iconos del menu
	private String[] lateralMenuTitles;
	private TypedArray lateralMenuIcons;
	//Nombre aplicación
	private CharSequence appTitle;
	//Items y adapter del menu lateral
	private ArrayList<LateralMenuItem> lateralMenuItems;
	private LateralMenuAdapter lateralMenuAdapter;

	@SuppressLint({ "NewApi", "InflateParams" }) @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Guardamos el titulo
		appTitle = lateralMenuTitle = getTitle();

		//Cargamos nombres e iconos
		lateralMenuTitles = getResources().getStringArray(R.array.lateral_menu_items);
		lateralMenuIcons = getResources().obtainTypedArray(R.array.lateral_menu_icons);

		//Obtenemos el DrawerLayout y Listview del menu del menu lateral
		lateralMenuDrawerLayout = (DrawerLayout) findViewById(R.id.lateral_menu_drawer_layout);
		lateralMenuListView = (ListView) findViewById(R.id.lateral_menu_list_view);
		lateralMenuItems = new ArrayList<LateralMenuItem>();

		// Agregar los items que llevara el menu
		lateralMenuItems.add(new LateralMenuItem(lateralMenuTitles[0], lateralMenuIcons.getResourceId(0, -1)));
		lateralMenuItems.add(new LateralMenuItem(lateralMenuTitles[1], lateralMenuIcons.getResourceId(1, -1)));
		lateralMenuItems.add(new LateralMenuItem(lateralMenuTitles[2], lateralMenuIcons.getResourceId(2, -1)));
		
		//Reciclamos los icocnos
		lateralMenuIcons.recycle();
		
		//seteamos Listener
		lateralMenuListView.setOnItemClickListener(new SlideMenuClickListener());

		// Crear y agregar el adapter
		lateralMenuAdapter = new LateralMenuAdapter(getApplicationContext(),lateralMenuItems);
		lateralMenuListView.setAdapter(lateralMenuAdapter);

		//Cambiamos el actionbar a nuestra vista personalizada
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater inflater = LayoutInflater.from(this);

		View mCustomView = inflater.inflate(R.layout.custom_actionbar, null);
		actionBar.setCustomView(mCustomView);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
		// Seteamos el icono del actionbar como clickeable e invisible
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));   
		
		
		//Agregamos el drawerToggle
		lateralMenuDrawerToggle = new ActionBarDrawerToggle(this, lateralMenuDrawerLayout,
				R.drawable.hamburger_icon, 
				R.string.app_name,
				R.string.app_name 
				) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(appTitle);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(lateralMenuTitle);
				invalidateOptionsMenu();
			}
		};
		lateralMenuDrawerLayout.setDrawerListener(lateralMenuDrawerToggle);

		if (savedInstanceState == null) {
			//seteamos la vista a la principal
			displayView(0);
		}
	}

	/**
	 * Listener del click de menu
	 * */
	private class SlideMenuClickListener implements
	ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// cambiamos la vista
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// mostramos la barra si se hace click
		if (lateralMenuDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/***
	 * Llamado para InvalidOptions
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean drawerOpen = lateralMenuDrawerLayout.isDrawerOpen(lateralMenuListView);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Mostramos los fragmentos
	 * */
	@SuppressLint("NewApi") private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new TaskListFragment();
			break;
		case 1:
			fragment = new ChartsFragment();
			break;
		case 2:
			fragment = new UserFragment();
			break;

		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
			.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			lateralMenuListView.setItemChecked(position, true);
			lateralMenuListView.setSelection(position);
			setTitle(lateralMenuTitles[position]);
			lateralMenuDrawerLayout.closeDrawer(lateralMenuListView);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@SuppressLint("NewApi") @Override
	public void setTitle(CharSequence title) {
		appTitle = title;
		getActionBar().setTitle(appTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		lateralMenuDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		lateralMenuDrawerToggle.onConfigurationChanged(newConfig);
	}

}