/*
 * Source code from Tectonicus, http://code.google.com/p/tectonicus/
 *
 * Tectonicus is released under the BSD license (below).
 *
 *
 * Original code John Campbell / "Orangy Tang" / www.triangularpixels.com
 *
 * Copyright (c) 2012, John Campbell
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright notice, this list
 *     of conditions and the following disclaimer.
 *
 *   * Redistributions in binary form must reproduce the above copyright notice, this
 *     list of conditions and the following disclaimer in the documentation and/or
 *     other materials provided with the distribution.
 *   * Neither the name of 'Tectonicus' nor the names of
 *     its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package tectonicus.blockTypes;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import tectonicus.BlockContext;
import tectonicus.BlockType;
import tectonicus.BlockTypeRegistry;
import tectonicus.Chunk;
import tectonicus.configuration.LightFace;
import tectonicus.rasteriser.SubMesh;
import tectonicus.rasteriser.SubMesh.Rotation;
import tectonicus.raw.RawChunk;
import tectonicus.renderer.Geometry;
import tectonicus.texture.SubTexture;

public class PistonBase implements BlockType
{
	private final String name;
	private SubTexture baseSide, top, bottom, pistonFace, pistonEdge;
	
	public PistonBase(String name, SubTexture side, SubTexture top, SubTexture bottom, SubTexture pistonFace)
	{
		this.name = name;
		
		this.top = top;
		this.bottom = bottom;
		this.pistonFace = pistonFace;
		
		final float divide;
		if (side.texturePackVersion == "1.4")
			divide = 1.0f / 16.0f / 16.0f * 4.0f;
		else
			divide = 1.0f / 16.0f * 4.0f;
		
		baseSide = new SubTexture(side.texture, side.u0, side.v0+divide, side.u1, side.v1);
		pistonEdge = new SubTexture(side.texture, side.u0, side.v0, side.u1, side.v0+divide);
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public boolean isSolid()
	{
		return false;
	}
	
	@Override
	public boolean isWater()
	{
		return false;
	}
	
	@Override
	public void addInteriorGeometry(int x, int y, int z, BlockContext world, BlockTypeRegistry registry, RawChunk chunk, Geometry geometry)
	{
		addEdgeGeometry(x, y, z, world, registry, chunk, geometry);
	}
	
	@Override
	public void addEdgeGeometry(int x, int y, int z, BlockContext world, BlockTypeRegistry registry, RawChunk chunk, Geometry geometry)
	{
		final int data = chunk.getBlockData(x, y, z);
		
		final boolean isExtended = (data & 0x8) > 0;
		final int direction = data & 0x7;
		
		SubMesh subMesh = new SubMesh();
		SubMesh pistonFaceMesh = null;
		
		final float lightness = Chunk.getLight(world.getLightStyle(), LightFace.Top, chunk, x, y, z);
		Vector4f colour = new Vector4f(lightness, lightness, lightness, 1);
		
		final float height = 1.0f / 16.0f * 12.0f;
		
		// Piston base

		SubMesh topMesh = new SubMesh();
		// Top
		topMesh.addQuad(new Vector3f(0, height, 0), new Vector3f(1, height, 0),
						new Vector3f(1, height, 1), new Vector3f(0, height, 1), colour, top);
		
		SubMesh bottomMesh = new SubMesh();
		// Bottom
		bottomMesh.addQuad(new Vector3f(0, 0, 0), new Vector3f(0, 0, 1),
						new Vector3f(1, 0, 1), new Vector3f(1, 0, 0), colour, bottom);
	
		SubMesh baseMesh = new SubMesh();
		// West
		baseMesh.addQuad(new Vector3f(0, height, 0), new Vector3f(0, height, 1),
						new Vector3f(0, 0, 1),  new Vector3f(0, 0, 0), colour, baseSide);
		// North
		baseMesh.addQuad(new Vector3f(1, height, 0), new Vector3f(0, height, 0),
						new Vector3f(0, 0, 0),  new Vector3f(1, 0, 0), colour, baseSide);
		// South
		baseMesh.addQuad(new Vector3f(0, height, 1), new Vector3f(1, height, 1),
						new Vector3f(1, 0, 1),  new Vector3f(0, 0, 1), colour, baseSide);
		// East
		baseMesh.addQuad(new Vector3f(1, height, 1), new Vector3f(1, height, 0),
						new Vector3f(1, 0, 0), new Vector3f(1, 0, 1), colour, baseSide);
		
		if (isExtended)
		{
			// base of piston arm?
		}
		else
		{
			// Unextended piston top
			
			pistonFaceMesh = new SubMesh();
			// Top
			pistonFaceMesh.addQuad(new Vector3f(0, 1, 0), new Vector3f(1, 1, 0),
							new Vector3f(1, 1, 1), new Vector3f(0, 1, 1), colour, pistonFace);
			// West
			subMesh.addQuad(new Vector3f(0, 1, 0), new Vector3f(0, 1, 1),
							new Vector3f(0, height, 1),  new Vector3f(0, height, 0), colour, pistonEdge);
			// North
			subMesh.addQuad(new Vector3f(1, 1, 0), new Vector3f(0, 1, 0),
							new Vector3f(0, height, 0),  new Vector3f(1, height, 0), colour, pistonEdge);
			// South
			subMesh.addQuad(new Vector3f(0, 1, 1), new Vector3f(1, 1, 1),
							new Vector3f(1, height, 1),  new Vector3f(0, height, 1), colour, pistonEdge);
			// East
			subMesh.addQuad(new Vector3f(1, 1, 1), new Vector3f(1, 1, 0),
							new Vector3f(1, height, 0), new Vector3f(1, height, 1), colour, pistonEdge);
		}
		
		Rotation horizRotation = Rotation.Clockwise;
		float horizAngle = 0;
		
		Rotation vertRotation = Rotation.None;
		float vertAngle = 0;
	
		// Set angle/rotation from block data flags
		if (direction == 0)
		{
			// down
			vertRotation = Rotation.Clockwise;
			vertAngle = 180;
		}
		else if (direction == 1)
		{
			// up
			// ...unchanged
		}
		else if (direction == 2)
		{
			// north
			vertRotation = Rotation.Clockwise;
			vertAngle = 90;
			
			horizRotation = Rotation.AntiClockwise;
			horizAngle = 90;
		}
		else if (direction == 3)
		{
			// south
			vertRotation = Rotation.Clockwise;
			vertAngle = 90;
			
			horizRotation = Rotation.Clockwise;
			horizAngle = 90;
		}
		else if (direction == 4)
		{
			// west
			vertRotation = Rotation.Clockwise;
			vertAngle = 90;
		}
		else if (direction == 5)
		{
			// east
			vertRotation = Rotation.Clockwise;
			vertAngle = 90;
			
			horizRotation = Rotation.Clockwise;
			horizAngle = 180;
		}
		
		baseMesh.pushTo(geometry.getMesh(baseSide.texture, Geometry.MeshType.Solid), x, y, z, horizRotation, horizAngle, vertRotation, vertAngle);
		subMesh.pushTo(geometry.getMesh(pistonEdge.texture, Geometry.MeshType.Solid), x, y, z, horizRotation, horizAngle, vertRotation, vertAngle);
		topMesh.pushTo(geometry.getMesh(top.texture, Geometry.MeshType.Solid), x, y, z, horizRotation, horizAngle, vertRotation, vertAngle);
		bottomMesh.pushTo(geometry.getMesh(bottom.texture, Geometry.MeshType.Solid), x, y, z, horizRotation, horizAngle, vertRotation, vertAngle);
		if(pistonFaceMesh != null)
			pistonFaceMesh.pushTo(geometry.getMesh(pistonFace.texture, Geometry.MeshType.Solid), x, y, z, horizRotation, horizAngle, vertRotation, vertAngle);
	}
}
