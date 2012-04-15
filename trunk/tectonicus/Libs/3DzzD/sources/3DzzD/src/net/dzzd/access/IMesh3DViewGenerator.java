/*
* This file is part of 3DzzD http://dzzd.net/.
*
* Released under LGPL
*
* 3DzzD is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* 3DzzD is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with 3DzzD.  If not, see <http://www.gnu.org/licenses/>.
*
* Copyright 2005 - 2009 Bruno Augier
*/

package net.dzzd.access;

/** 
 *  Used for accessing to a Mesh3DViewGenerator.
 *  <br>
 *  IMesh3DViewGenerator are able to give a subset of Face3D as a Face3DList for a given view point.
 *  
 *  @author Bruno Augier
 *  @version 1.0, 01/01/04
 *  @since 1.0
 *	@see IMesh3D
 */
public interface IMesh3DViewGenerator
{
	/**
	 * Sets this Mesh3DViewGenerator quality.
	 * <br>
	 * quality is the pixel precision to use for view generation<br>
	 * 
	 * @param quality quality
	 */		
	public void setMesh3DViewGeneratorQuality(double quality);

	/**
	 * Gets this Mesh3DViewGenerator current quality.
	 * <br>
	 * quality is the pixel precision used for view generation<br>
	 * 
	 * @return current quality
	 */		
	public double getMesh3DViewGeneratorQuality();

	/**
	 * Generate a (LOD) Face3DList for a given view point using the current view generator quality.
	 * <br>
	 * View point must be given in object space
	 * <br>
	 * 
	 * @param x view point x in object space
	 * @param y view point y in object space
	 * @param z view point z in object space
	 * @param tx view point target x in object space
	 * @param ty view point target y in object space
	 * @param tz view point target z in object space
	 * @param focus focal length to use
	 * @param viewWidth screen width in pixels
	 */		
	public void generateForView(double x,double y,double z,double tx,double ty,double tz,double focus,double viewWidth);

	/**
	 * Gets number of Face3D generated by the last call to generateForView.
	 * <br>
	 * @return number of Face3D that was generated for last call to generateForView
	 */		
	public int getNbViewFaces();
	
	/**
	 * Gets Face3DList generated by the last call to generateForView.
	 * <br>
	 * @return Face3DList that was generated for last call to generateForView
	 */	
	public IFace3DList getViewFace3DList();

	/**
	 * Sets far clipping value for view generation.
	 *
	 * @param farClip far clipping value
	 */
	public void setFarClip(double farClip);

	/**
	 * Sets near clipping value for view generation.
	 *
	 * @param nearClip near clipping value
	 */
	public void setNearClip(double nearClip);

}