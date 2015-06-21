package bourgeoisarab.divinealchemy;

import bourgeoisarab.divinealchemy.common.event.AIEventHooks;
import bourgeoisarab.divinealchemy.common.item.BucketHandler;
import bourgeoisarab.divinealchemy.init.ConfigHandler;
import bourgeoisarab.divinealchemy.init.ModBlocks;
import bourgeoisarab.divinealchemy.init.ModEntities;
import bourgeoisarab.divinealchemy.init.ModFluids;
import bourgeoisarab.divinealchemy.init.ModItems;
import bourgeoisarab.divinealchemy.init.crafting.Recipes;
import bourgeoisarab.divinealchemy.network.NetworkHandler;
import bourgeoisarab.divinealchemy.proxy.CommonProxy;
import bourgeoisarab.divinealchemy.reference.Ref;
import bourgeoisarab.divinealchemy.troll.CommandCake;
import bourgeoisarab.divinealchemy.troll.TrollEvents;
import bourgeoisarab.divinealchemy.utility.ModPotionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Ref.MODID, name = Ref.NAME, version = Ref.VERSION)
public class DivineAlchemy {

	@Mod.Instance(Ref.MODID)
	public static DivineAlchemy instance;

	@SidedProxy(clientSide = Ref.CLIENT_PROXY, serverSide = Ref.SERVER_PROXY)
	public static CommonProxy proxy;

	public static CreativeTabs tabAInstillation = new CreativeTabs("tabDivineAlchemy") {
		@Override
		public Item getTabIconItem() {
			return Item.getItemFromBlock(ModBlocks.blockBrewingCauldron);
		}

		@Override
		public int func_151243_f() {
			return 4;
		}
	};

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		expandPotionArray(256);

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
		proxy.preInit();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init();
		Recipes.init();
		ModPotionHelper.init();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit();
		MinecraftForge.EVENT_BUS.register(new BucketHandler());
		AIEventHooks eventHooks = new AIEventHooks();
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
	}

	public static void expandPotionArray(int arraySize) {
		Potion[] potionTypes = null;
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
				}
			} catch (Exception e) {
				System.err.println("BourgeoisArab made a serious boo-boo. Please report this ASAP:");
				System.err.println(e);
			}
		}
	}

}
