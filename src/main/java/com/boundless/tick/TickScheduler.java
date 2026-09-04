package com.boundless.tick;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TickScheduler {
    private static final List<ScheduledTask> TASKS = new ArrayList<>();

    private record ScheduledTask(long executeAtTick, Runnable task) {}

    public static void schedule(World world, int delayTicks, Runnable task) {
        if (!(world instanceof ServerWorld serverWorld)) return;

        long executeAtTick = serverWorld.getServer().getTicks() + delayTicks;
        TASKS.add(new ScheduledTask(executeAtTick, task));
    }

    public static void tick(MinecraftServer server) {
        if (TASKS.isEmpty()) return;
        long currentTick = server.getTicks();

        List<ScheduledTask> ready = new ArrayList<>();
        Iterator<ScheduledTask> iterator = TASKS.iterator();

        while (iterator.hasNext()) {
            ScheduledTask task = iterator.next();
            if (currentTick >= task.executeAtTick()) {
                ready.add(task);
                iterator.remove();
            }
        }

        for (ScheduledTask scheduledTask: ready) {
            scheduledTask.task().run();
        }
    }
}
