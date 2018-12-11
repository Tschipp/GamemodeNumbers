package tschipp.gamemodenumbers.mixin;

import java.util.Collection;
import java.util.Collections;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.server.command.GameModeCommand;
import net.minecraft.server.command.ServerCommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;

@Mixin(GameModeCommand.class)
public class GamemodeCommandMixin {

	@Shadow
	private static void method_13390(ServerCommandSource var0, ServerPlayerEntity var1, GameMode var2) {
	}

	@Shadow
	private static int method_13387(CommandContext<ServerCommandSource> var0, Collection<ServerPlayerEntity> var1,
			GameMode var2) {
		return 0;
	}

	@Inject(at = @At("HEAD"), method = "register")
	private static void onRegister(CommandDispatcher<ServerCommandSource> var0, CallbackInfo info) {
		LiteralArgumentBuilder<ServerCommandSource> var1 = ServerCommandManager.literal("gamemode")
				.requires((var0x) -> {
					return var0x.hasPermissionLevel(2);
				});
				
		var1.then((ServerCommandManager.argument("gamemode", IntegerArgumentType.integer(0, 3)).executes((var1x) -> {
			return method_13387(var1x, Collections.singleton(((ServerCommandSource) var1x.getSource()).getPlayer()),
					GameMode.byId(var1x.getArgument("gamemode", Integer.class), GameMode.SURVIVAL));
		})).then(ServerCommandManager.argument("target", EntityArgumentType.method_9308()).executes((var1x) -> {
			return method_13387(var1x, EntityArgumentType.method_9312(var1x, "target"),
					GameMode.byId(var1x.getArgument("gamemode", Integer.class), GameMode.SURVIVAL));
		})));

		var0.register(var1);
	}

}
