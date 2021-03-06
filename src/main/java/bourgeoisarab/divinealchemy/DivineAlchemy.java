package bourgeoisarab.divinealchemy;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import bourgeoisarab.divinealchemy.command.CommandDivinity;
import bourgeoisarab.divinealchemy.common.event.BucketHandler;
import bourgeoisarab.divinealchemy.common.event.DAEventHooks;
import bourgeoisarab.divinealchemy.common.event.StartupEventHooks;
import bourgeoisarab.divinealchemy.common.potion.ingredient.PotionIngredient;
import bourgeoisarab.divinealchemy.common.worldgen.WorldGenObeliskDark;
import bourgeoisarab.divinealchemy.init.ConfigHandler;
import bourgeoisarab.divinealchemy.init.ModBlocks;
import bourgeoisarab.divinealchemy.init.ModEntities;
import bourgeoisarab.divinealchemy.init.ModFluids;
import bourgeoisarab.divinealchemy.init.ModItems;
import bourgeoisarab.divinealchemy.init.crafting.Recipes;
import bourgeoisarab.divinealchemy.network.NetworkHandler;
import bourgeoisarab.divinealchemy.network.NetworkHandlerDescription;
import bourgeoisarab.divinealchemy.proxy.CommonProxy;
import bourgeoisarab.divinealchemy.reference.Ref;
import bourgeoisarab.divinealchemy.troll.CommandCake;
import bourgeoisarab.divinealchemy.troll.TrollEvents;
import bourgeoisarab.divinealchemy.utility.Log;
import bourgeoisarab.divinealchemy.utility.ModPotionHelper;

@Mod(modid = Ref.MODID, name = Ref.NAME, version = Ref.VERSION)
public class DivineAlchemy {

	@Mod.Instance(Ref.MODID)
	public static DivineAlchemy instance;

	@SidedProxy(clientSide = Ref.CLIENT_PROXY, serverSide = Ref.SERVER_PROXY)
	public static CommonProxy proxy;

	public static CreativeTabs tabDivineAlchemy = new CreativeTabs("tabDivineAlchemy") {

		@Override
		public Item getTabIconItem() {
			return Item.getItemFromBlock(ModBlocks.brewingCauldron);
		}

		@Override
		public int getIconItemDamage() {
			return 4;
		}
	};

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new StartupEventHooks());

		// expandPotionArray(128);

		ConfigHandler.init(event.getSuggestedConfigurationFile());

		ModFluids.init();
		ModFluids.register();

		ModBlocks.init();
		ModBlocks.registerBlocks();
		ModBlocks.registerTileEntities();

		ModItems.init();
		ModItems.registerItems();

		ModEntities.init();

		NetworkHandler.init();
		NetworkHandlerDescription.init();
		proxy.preInit();

		GameRegistry.registerWorldGenerator(new WorldGenObeliskDark(), 0);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init();
		Recipes.init();
		ModPotionHelper.init();
		Log.info("Registered " + PotionIngredient.ingredients.size() + " ingredients");

	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit();
		MinecraftForge.EVENT_BUS.register(new BucketHandler());
		DAEventHooks eventHooks = new DAEventHooks();
		MinecraftForge.EVENT_BUS.register(eventHooks);
		FMLCommonHandler.instance().bus().register(eventHooks);
		try {
			FMLCommonHandler.instance().bus().register(new TrollEvents());
		} catch (Exception e) {
		}
	}

	@Mod.EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		try {
			event.registerServerCommand(new CommandCake());
		} catch (Exception e) {
		}
		event.registerServerCommand(new CommandDivinity());
	}

	@Deprecated
	public static void expandPotionArray(int arraySize) {
		if (arraySize > 128) {
			Log.warn("Potion array can't be bigger than 128. Setting to 128");
			arraySize = 128;
		}
		Potion[] potionTypes = null;
		boolean successful = false;
		for (Field f : Potion.class.getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getName().equals("potionTypes") || f.getName().equals("field_76425_a")) {
					Field modfield = Field.class.getDeclaredField("modifiers");
					modfield.setAccessible(true);
					modfield.setInt(f, f.getModifiers() & ~Modifier.FINAL);
					potionTypes = (Potion[]) f.get(null);
					final Potion[] newPotionTypes = new Potion[arraySize];
					System.arraycopy(potionTypes, 0, newPotionTypes, 0, potionTypes.length);
					f.set(null, newPotionTypes);
					successful = true;
				}
			} catch (Exception e) {
				System.err.println("BourgeoisArab made a serious boo-boo. Please report this ASAP:");
				e.printStackTrace();
			}
		}
		if (successful) {
			Log.info("Potion array was expanded to " + arraySize);
		}
	}

}
