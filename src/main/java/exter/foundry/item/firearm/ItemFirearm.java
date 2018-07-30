package exter.foundry.item.firearm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.tuple.MutablePair;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.firearms.IFirearmRound;
import exter.foundry.creativetab.FoundryTabFirearms;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public abstract class ItemFirearm extends Item {

	static private Random random = new Random();

	static public boolean roundMatches(ItemStack stack, String type) {
		if (stack.isEmpty()) { return false; }
		if (!stack.hasCapability(FoundryAPI.FIREARM_ROUND_CAP, null)) { return false; }
		return stack.getCapability(FoundryAPI.FIREARM_ROUND_CAP, null).getRoundType().equals(type);
	}

	static public final void shoot(ItemStack round_item, World world, EntityLivingBase shooter, Entity target, int times, float spread, float damage_multiplier) {
		Map<EntityLivingBase, MutablePair<Float, Integer>> entities_hit = new HashMap<>();
		IFirearmRound round = round_item.getCapability(FoundryAPI.FIREARM_ROUND_CAP, null);
		int i;
		for (i = 0; i < times; i++) {
			RayTraceResult obj = trace(world, shooter, target, spread);
			if (obj != null) {
				switch (obj.typeOfHit) {
				case BLOCK:
					IBlockState b = world.getBlockState(obj.getBlockPos());
					if (round.breaksGlass() && b.getMaterial() == Material.GLASS && b.getBlockHardness(world, obj.getBlockPos()) < 0.4) {
						world.playEvent(null, 2001, obj.getBlockPos(), Block.getIdFromBlock(b.getBlock()) + (b.getBlock().getMetaFromState(b) << 12));
						if (!world.isRemote) {
							world.setBlockToAir(obj.getBlockPos());
						}
					} else {
						round.onBulletHitBlock(shooter, (Vec3d) obj.hitInfo, world, obj.getBlockPos(), obj.sideHit);
					}
					break;
				case ENTITY:
					if (obj.entityHit instanceof EntityLivingBase) {
						Vec3d end = new Vec3d(obj.entityHit.posX, obj.entityHit.posY, obj.entityHit.posZ);
						double distance = end.distanceTo((Vec3d) obj.hitInfo);
						double base_range = round.getBaseRange();
						double falloff_range = round.getFalloffRange();
						double base_damage = round.getBaseDamage((EntityLivingBase) obj.entityHit);
						double damage;
						if (distance < base_range) {
							damage = base_damage;
						} else if (distance > base_range + falloff_range) {
							damage = 0;
						} else {
							damage = base_damage * (1.0f - (distance - base_range) / falloff_range);
						}
						damage *= damage_multiplier;
						if (damage >= 1) {
							MutablePair<Float, Integer> accum = entities_hit.get(obj.entityHit);
							if (accum == null) {
								accum = new MutablePair<>(0.0f, 0);
								entities_hit.put((EntityLivingBase) obj.entityHit, accum);
							}
							accum.left += (float) damage;
							accum.right++;
						}
					}
					break;
				default:
					break;
				}
			}
		}
		for (Map.Entry<EntityLivingBase, MutablePair<Float, Integer>> hit : entities_hit.entrySet()) {
			EntityLivingBase en = hit.getKey();
			DamageSource damage = new EntityDamageSource("foundry.bullet", shooter).setProjectile();
			if (round.ignoresArmor()) {
				damage.setDamageBypassesArmor();
			}
			if (en.attackEntityFrom(damage, hit.getValue().left)) {
				round.onBulletDamagedLivingEntity(en, hit.getValue().right);
			}
		}
	}

	static private RayTraceResult trace(World world, EntityLivingBase shooter, Entity target, float spread) {
		Vec3d start = new Vec3d(shooter.posX, shooter.posY + shooter.getEyeHeight() - 0.1, shooter.posZ);
		Vec3d dir;
		if (target != null) {
			dir = new Vec3d(target.posX - start.x, target.posY - start.y, target.posZ - start.z).normalize();
		} else {
			float pitch = -shooter.rotationPitch;
			float yaw = -shooter.rotationYaw;
			float cyaw = MathHelper.cos(yaw * 0.017453292F - (float) Math.PI);
			float syaw = MathHelper.sin(yaw * 0.017453292F - (float) Math.PI);
			float cpitch = -MathHelper.cos(pitch * 0.017453292F);

			dir = new Vec3d(syaw * cpitch, MathHelper.sin(pitch * 0.017453292F), cyaw * cpitch);
		}
		Vec3d vspread = new Vec3d(random.nextFloat() * 2 - 1, random.nextFloat() * 2 - 1, random.nextFloat() * 2 - 1).normalize();
		spread = random.nextFloat() * spread;
		dir = dir.add(vspread.x * spread, vspread.y * spread, vspread.z * spread).normalize();

		double distance = 150.0D;

		Vec3d end = start.add(dir.x * distance, dir.y * distance, dir.z * distance);

		Vec3d tstart = new Vec3d(start.x, start.y, start.z);
		Vec3d tend = new Vec3d(end.x, end.y, end.z);
		RayTraceResult obj = world.rayTraceBlocks(tstart, tend, false, true, false);

		List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(shooter, shooter.getEntityBoundingBox().grow(150, 150, 150));
		double min_dist = obj != null ? obj.hitVec.distanceTo(start) : 150;
		for (Entity ent : entities) {
			if (ent.canBeCollidedWith() && ent.getEntityBoundingBox() != null) {
				RayTraceResult ent_obj = ent.getEntityBoundingBox().grow(0.1, 0.1, 0.1).calculateIntercept(start, end);
				if (ent_obj != null) {
					if (ent_obj.typeOfHit == RayTraceResult.Type.BLOCK) {
						ent_obj.typeOfHit = RayTraceResult.Type.ENTITY;
						ent_obj.entityHit = ent;
					}
					double d = ent_obj.hitVec.distanceTo(start);
					if (obj == null || d < min_dist) {
						min_dist = d;

						obj = ent_obj;
					}
				}
			}
		}
		if (obj != null) {
			obj.hitInfo = start;
		}
		return obj;
	}

	public ItemFirearm() {
		setMaxDamage(800);
		setCreativeTab(FoundryTabFirearms.INSTANCE);
		setMaxStackSize(1);
		MinecraftForge.EVENT_BUS.register(this);
	}

	public abstract ItemStack getAmmo(ItemStack stack, int slot);

	@Override
	public final int getItemStackLimit(ItemStack stack) {
		return 1;
	}

	@Override
	public final EnumAction getItemUseAction(ItemStack p_77661_1_) {
		return EnumAction.BOW;
	}

	@Override
	public final int getMaxItemUseDuration(ItemStack p_77626_1_) {
		return 72000;
	}

	public abstract void setAmmo(ItemStack stack, int slot, ItemStack ammo);
}
