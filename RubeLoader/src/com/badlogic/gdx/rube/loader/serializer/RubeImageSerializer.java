package com.badlogic.gdx.rube.loader.serializer;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.rube.RubeCustomProperty;
import com.badlogic.gdx.rube.RubeImage;
import com.badlogic.gdx.rube.loader.RubeDefaults;
import com.badlogic.gdx.rube.loader.serializer.utils.RubeVertexArray;
import com.badlogic.gdx.utils.Json;

/**
 * Serializer to read a {@link RubeImage} from a RUBE .json file.
 * @author clement.vayer
 */
@SuppressWarnings("rawtypes")
class RubeImageSerializer extends RubeSerializer<RubeImage>
{	
	public RubeImageSerializer() 
	{
		super();
	}
	
	private final Vector2 tmp = new Vector2();
	@Override
	public RubeImage read(Json json, Object jsonData, Class type) 
	{
		if(scene.getBodies() == null)
			return null;
		
		RubeImage defaults = RubeDefaults.Image.image;
		
		RubeImage image = new RubeImage();
		
		image.angle = json.readValue("angle", float.class, defaults.angle, jsonData);
		int bodyIndex = json.readValue("body", int.class, jsonData);
		if(bodyIndex >= 0 && bodyIndex < scene.getBodies().size)
			image.body = scene.getBodies().get(bodyIndex);
		
		image.center.set(json.readValue("center", Vector2.class, defaults.center, jsonData));
		
		RubeVertexArray corners = json.readValue("corners", RubeVertexArray.class, jsonData);
		if(corners != null)
		{		
			tmp.set(corners.x[0],corners.y[0]).sub(corners.x[1], corners.y[1]);
	        image.width = tmp.len();
	        tmp.set(corners.x[1],corners.y[1]).sub(corners.x[2], corners.y[2]);
		    image.height = tmp.len();
		}
		
		image.file = json.readValue("file", String.class, jsonData);
		image.filter = json.readValue("filter", int.class, defaults.filter, jsonData);
		image.name = json.readValue("name", String.class, jsonData);
		image.opacity = json.readValue("opacity", float.class, defaults.opacity, jsonData);
		image.renderOrder = json.readValue("renderOrder", int.class, defaults.renderOrder, jsonData);
		image.scale = json.readValue("scale", float.class, defaults.scale, jsonData);
		
		int [] colorArray = json.readValue("colorTint", int[].class,RubeDefaults.Image.colorArray,jsonData);
	    image.color.set((float)colorArray[0]/255, (float)colorArray[1]/255, (float)colorArray[2]/255, (float)colorArray[3]/255);
	      
		
		RubeCustomProperty customProperty = null;
		if(json.getSerializer(RubeCustomProperty.class) != null)
			customProperty = json.readValue("customProperties", RubeCustomProperty.class, jsonData);
		
		scene.onAddImage(image, customProperty);
		
		return image;
	}

}
