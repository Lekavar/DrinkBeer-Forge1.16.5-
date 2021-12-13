package lekavar.lma.drinkbeer.client.renderer;

import lekavar.lma.drinkbeer.client.model.BeerBlockModel;
import lekavar.lma.drinkbeer.blocks.tileentity.BeerTileEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class BeerBlockRenderer extends GeoBlockRenderer<BeerTileEntity> {
    public BeerBlockRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn, new BeerBlockModel());
    }
}
