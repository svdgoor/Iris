/*
 * Iris is a World Generator for Minecraft Bukkit Servers
 * Copyright (c) 2021 Arcane Arts (Volmit Software)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.volmit.iris.core.commands;

import com.volmit.iris.Iris;
import com.volmit.iris.core.gui.PregeneratorJob;
import com.volmit.iris.core.pregenerator.PregenTask;
import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.util.decree.DecreeExecutor;
import com.volmit.iris.util.decree.annotations.Decree;
import com.volmit.iris.util.decree.annotations.Param;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.math.Position2;
import org.bukkit.World;
import org.bukkit.util.Vector;

@Decree(name = "pregen", aliases = "pregenerate", description = "Pregenerate your Iris worlds!")
public class CommandPregen implements DecreeExecutor {
    @Decree(description = "Pregenerate a world")
    public void start(
            @Param(description = "The world to pregen", contextual = true)
                    World world,
            @Param(description = "The radius of the pregen in blocks", aliases = "size")
                    int radius,
            @Param(aliases = "middle", description = "The center location of the pregen. Use \"me\" for your current location", defaultValue = "0,0")
                    Vector center
    ) {
        try {
            IrisToolbelt.pregenerate(PregenTask
                    .builder()
                    .center(new Position2(center))
                    .width((radius >> 9 + 1) * 2)
                    .height((radius >> 9 + 1) * 2)
                    .build(), world);
            sender().sendMessage(C.GREEN + "Successfully started the pregeneration task!");
        } catch (Throwable e) {
            sender().sendMessage(C.RED + "Epic fail");
            Iris.reportError(e);
            e.printStackTrace();
        }
    }

    @Decree(description = "Stop the active pregeneration task", aliases = "x")
    public void stop() {
        if (PregeneratorJob.shutdownInstance()) {
            sender().sendMessage(C.GREEN + "Stopped pregeneration task");
        } else {
            sender().sendMessage(C.YELLOW + "No active pregeneration tasks to stop");
        }
    }

    @Decree(description = "Pause / continue the active pregeneration task", aliases = {"resume", "unpause"})
    public void pause() {
        if (PregeneratorJob.pauseResume()) {
            sender().sendMessage(C.GREEN + "Paused/unpaused pregeneration task, now: " + (PregeneratorJob.isPaused() ? "Paused" : "Running") + ".");
        } else {
            sender().sendMessage(C.YELLOW + "No active pregeneration tasks to pause/unpause.");
        }
    }
}