package util;

import protonova.protobuf.CustomDataProto.CustomData;
import protonova.protobuf.EntityProto.Entity;

public class DataUtil {
	
	/*
	 * All of these classes are pretty self explanatory so no comments for you
	 * 
	 * on important thing is that if the custom data is set even if you dont setup a field it will have a value probaly like zero or "" so itll return that instead
	 * of your provided default value fyi
	 */
	
	public static int getInt(Entity entity, String dataKey, int defaultValue) {
		CustomData customData = entity.getCustomDataMap().get(dataKey);
		
		if (customData != null) return customData.getIntValue();
		else return defaultValue;
	}
	
	public static void setInt(Entity.Builder entity, String dataKey, int value) {
		CustomData customData = entity.getCustomDataMap().get(dataKey);
		
		if (customData == null) {
			customData = CustomData.newBuilder().setIntValue(value).build();
		}
		else {
			customData = customData.toBuilder().setIntValue(value).build();
		}
	}
	
	public static Entity setInt(Entity entity, String dataKey, int value) {
		Entity.Builder builder = entity.toBuilder();
		setInt(builder,dataKey,value);
		return builder.build();
	}
	
	public static CustomData newInt(int value) {
		return CustomData.newBuilder().setIntValue(value).build();
	}
	
	
	
	
	public static String getString(Entity entity, String dataKey, String defaultValue) {
		CustomData customData = entity.getCustomDataMap().get(dataKey);
		
		if (customData != null) return customData.getStringValue();
		else return defaultValue;
	}
	
	public static void setString(Entity.Builder entity, String dataKey, String value) {
		CustomData customData = entity.getCustomDataMap().get(dataKey);
		
		if (customData == null) {
			customData = CustomData.newBuilder().setStringValue(value).build();
		}
		else {
			customData = customData.toBuilder().setStringValue(value).build();
		}
	}
	
	public static Entity setString(Entity entity, String dataKey, String value) {
		Entity.Builder builder = entity.toBuilder();
		setString(builder,dataKey,value);
		return builder.build();
	}
	
	public static CustomData newString(String value) {
		return CustomData.newBuilder().setStringValue(value).build();
	}
	
	
	
	
	public static float getFloat(Entity entity, String dataKey, float defaultValue) {
		CustomData customData = entity.getCustomDataMap().get(dataKey);
		
		if (customData != null) return customData.getFloatValue();
		else return defaultValue;
	}
	
	public static void setFloat(Entity.Builder entity, String dataKey, float value) {
		CustomData customData = entity.getCustomDataMap().get(dataKey);
		
		if (customData == null) {
			customData = CustomData.newBuilder().setFloatValue(value).build();
		}
		else {
			customData = customData.toBuilder().setFloatValue(value).build();
		}
	}
	
	public static Entity setFloat(Entity entity, String dataKey, float value) {
		Entity.Builder builder = entity.toBuilder();
		setFloat(builder,dataKey,value);
		return builder.build();
	}
	
	public static CustomData newFloat(float value) {
		return CustomData.newBuilder().setFloatValue(value).build();
	}
	
	
	
	public static double getDouble(Entity entity, String dataKey, double defaultValue) {
		CustomData customData = entity.getCustomDataMap().get(dataKey);
		
		if (customData != null) return customData.getDoubleValue();
		else return defaultValue;
	}
	
	public static void setDouble(Entity.Builder entity, String dataKey, double value) {
		CustomData customData = entity.getCustomDataMap().get(dataKey);
		
		if (customData == null) {
			customData = CustomData.newBuilder().setDoubleValue(value).build();
		}
		else {
			customData = customData.toBuilder().setDoubleValue(value).build();
		}
	}
	
	public static Entity setDouble(Entity entity, String dataKey, double value) {
		Entity.Builder builder = entity.toBuilder();
		setDouble(builder,dataKey,value);
		return builder.build();
	}
	
	public static CustomData newDouble(double value) {
		return CustomData.newBuilder().setDoubleValue(value).build();
	}
	
	
	
	
	
	public static boolean getBoolean(Entity entity, String dataKey, boolean defaultValue) {
		CustomData customData = entity.getCustomDataMap().get(dataKey);
		
		if (customData != null) return customData.getBooleanValue();
		else return defaultValue;
	}
	
	public static void setBoolean(Entity.Builder entity, String dataKey, boolean value) {
		CustomData customData = entity.getCustomDataMap().get(dataKey);
		
		if (customData == null) {
			customData = CustomData.newBuilder().setBooleanValue(value).build();
		}
		else {
			customData = customData.toBuilder().setBooleanValue(value).build();
		}
	}
	
	public static Entity setBoolean(Entity entity, String dataKey, boolean value) {
		Entity.Builder builder = entity.toBuilder();
		setBoolean(builder,dataKey,value);
		return builder.build();
	}
	
	public static CustomData newBoolean(boolean value) {
		return CustomData.newBuilder().setBooleanValue(value).build();
	}
}
