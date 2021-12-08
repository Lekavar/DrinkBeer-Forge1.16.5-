package lekavar.lma.drinkbeer.essentials.spice;

import com.google.common.collect.Lists;
import lekavar.lma.drinkbeer.essentials.flavor.BaseFlavors;
import lekavar.lma.drinkbeer.essentials.flavor.IFlavor;

import java.util.List;

public class Spices {
    public static ISpice BLAZE_PAPRIKA = new BlazePaprika();
    public static ISpice AMETHYST_NIGELLA_SEEDS = new AmethystNigellaSeeds();
    public static ISpice CITRINE_NIGELLA_SEEDS = new CitrineNigellaSeeds();
    public static ISpice DRIED_EGLIA_BUD = new DriedEgliaBud();
    public static ISpice SMOKED_EGLIA_BUD = new SmokedEgliaBud();
    public static ISpice ICE_MINT = new IceMint();
    public static ISpice ICE_PATCHOULI = new IcePatchouli();
    public static ISpice STORM_SHARDS = new StormShards();

    static class BlazePaprika implements ISpice {
        @Override
        public String getTranslationKey() {
            return "blaze_paprika";
        }

        @Override
        public List<IFlavor> getFlavor() {
            return Lists.newArrayList(BaseFlavors.FIERY);
        }
    }

    static class AmethystNigellaSeeds implements ISpice {
        @Override
        public String getTranslationKey() {
            return "amethyst_nigella_seeds";
        }

        @Override
        public List<IFlavor> getFlavor() {
            return Lists.newArrayList(BaseFlavors.AROMATIC);
        }
    }

    static class CitrineNigellaSeeds implements ISpice {
        @Override
        public String getTranslationKey() {
            return "critine_nigella_seed";
        }

        @Override
        public List<IFlavor> getFlavor() {
            return Lists.newArrayList(BaseFlavors.MORE_AROMATIC);
        }
    }


    static class DriedEgliaBud implements ISpice {
        @Override
        public String getTranslationKey() {
            return "dried_eglia_bud";
        }

        @Override
        public List<IFlavor> getFlavor() {
            return Lists.newArrayList(BaseFlavors.SPICY);
        }
    }

    static class SmokedEgliaBud implements ISpice {
        @Override
        public String getTranslationKey() {
            return "smoked_eglia_bud";
        }

        @Override
        public List<IFlavor> getFlavor() {
            return Lists.newArrayList(BaseFlavors.FIERY);
        }
    }

    static class IceMint implements ISpice {
        @Override
        public String getTranslationKey() {
            return "ice_mint";
        }

        @Override
        public List<IFlavor> getFlavor() {
            return Lists.newArrayList(BaseFlavors.REFRESHING);
        }
    }

    static class IcePatchouli implements ISpice {
        @Override
        public String getTranslationKey() {
            return "ice_patchouli";
        }

        @Override
        public List<IFlavor> getFlavor() {
            return Lists.newArrayList(BaseFlavors.MORE_REFRESHING);
        }
    }

    static class StormShards implements ISpice {
        @Override
        public String getTranslationKey() {
            return "storm_shard";
        }

        @Override
        public List<IFlavor> getFlavor() {
            return Lists.newArrayList(BaseFlavors.STORMY);
        }
    }
}
