package com.infinityraider.ninjagear.reference;

public interface Reference {
	
	String MOD_NAME = /*^${mod.name}^*/ "NinjaGear";
	String MOD_ID = /*^${mod.id}^*/ "ninja_gear";
	String AUTHOR = /*^${mod.author}^*/ "InfinityRaider";

	String VER_MAJOR = /*^${mod.version_major}^*/ "2";
	String VER_MINOR = /*^${mod.version_minor}^*/ "0";
	String VER_PATCH = /*^${mod.version_patch}^*/ "1";
	String MOD_VERSION = /*^${mod.version}^*/ "2.0.1";
	String VERSION = /*^${mod.version_minecraft}-${mod.version}^*/ "1.16.5-" + MOD_VERSION;
	
	String UPDATE_URL = /*^${mod.update_url}^*/ "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
	
}