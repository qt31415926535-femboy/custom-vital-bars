/*
 * Copyright (c) 2017, Seth <Sethtroll3@gmail.com>
 * Copyright (c) 2017, Adam <Adam@sigterm.info>
 * Copyright (c) 2018, Jordan Atwood <jordan.atwood423@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.customvitalbars;

import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.api.ItemID;

import javax.annotation.Nullable;
import java.time.Duration;
import java.time.temporal.TemporalUnit;


@Getter(AccessLevel.PACKAGE)
enum GameTimer
{
	STAMINA(ItemID.STAMINA_POTION4, "Stamina", false),
	PRAYER_REGENERATION(ItemID.PRAYER_REGENERATION_POTION4, "Prayer regeneration", false),
	;

	@Nullable
	private final Duration duration;
	@Nullable
	private final Integer graphicId;
	private final String description;
	private final boolean removedOnDeath;
	private final int imageId;

	GameTimer(int imageId, String description, Integer graphicId, long time, TemporalUnit unit, boolean removedOnDeath)
	{
		this.description = description;
		this.graphicId = graphicId;
		this.duration = Duration.of(time, unit);
		this.imageId = imageId;
		this.removedOnDeath = removedOnDeath;
	}

	GameTimer(int imageId, String description, long time, TemporalUnit unit, boolean removeOnDeath)
	{
		this(imageId, description, null, time, unit, removeOnDeath);
	}

	GameTimer(int imageId, String description, long time, TemporalUnit unit)
	{
		this(imageId, description, null, time, unit, false);
	}

	GameTimer(int imageId, String description, boolean removedOnDeath)
	{
		this.duration = null;
		this.graphicId = null;
		this.description = description;
		this.removedOnDeath = removedOnDeath;
		this.imageId = imageId;
	}
}
