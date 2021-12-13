package lekavar.lma.drinkbeer.client.model;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.blocks.tileentity.BeerTileEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BeerBlockModel extends AnimatedGeoModel<BeerTileEntity> {

    @Override
    public ResourceLocation getModelLocation(BeerTileEntity tileEntity) {
        return new ResourceLocation(tileEntity.getBeerType().getModId(), "geo/beer/" + tileEntity.getBeerType().getPath() + "_" + tileEntity.getCount() + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(BeerTileEntity tileEntity) {
        return new ResourceLocation(tileEntity.getBeerType().getModId(), "textures/beer/" + tileEntity.getBeerType().getPath() + "_" + tileEntity.getCount() + ".png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(BeerTileEntity tileEntity) {
        return new ResourceLocation(DrinkBeer.MODID, "animations/none.animation.json");
    }
}
