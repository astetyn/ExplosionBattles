package main.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import main.Game;
import main.maps.world.WorldsEB;

public class WeatherListener implements Listener {

	@EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
       
		WorldsEB gameWorld = Game.getInstance().getWorldsEB();
		
		if(gameWorld==null) {
			return;
		}
		
        if(event.getWorld().equals(gameWorld.getGameWorld())) {
            event.setCancelled(true);
        }
       
    }

}
