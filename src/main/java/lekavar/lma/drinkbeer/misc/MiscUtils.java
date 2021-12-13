package lekavar.lma.drinkbeer.misc;

import net.minecraft.particles.IParticleData;
import net.minecraft.world.server.ServerWorld;

public class MiscUtils {
    public static <T extends IParticleData> void addParticle(ServerWorld world, T particle, double x, double y, double z, double xDist, double yDist, double zDist, double maxSpeed, int count) {
        world.sendParticles(particle, x, y, z, count, xDist, yDist, zDist, maxSpeed);
    }
}
