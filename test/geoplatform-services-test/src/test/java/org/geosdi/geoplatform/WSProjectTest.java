/*
 *  geo-platform
 *  Rich webgis framework
 *  http://geo-platform.org
 * ====================================================================
 *
 * Copyright (C) 2008-2011 geoSDI Group (CNR IMAA - Potenza - ITALY).
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version. This program is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details. You should have received a copy of the GNU General
 * Public License along with this program. If not, see http://www.gnu.org/licenses/
 *
 * ====================================================================
 *
 * Linking this library statically or dynamically with other modules is
 * making a combined work based on this library. Thus, the terms and
 * conditions of the GNU General Public License cover the whole combination.
 *
 * As a special exception, the copyright holders of this library give you permission
 * to link this library with independent modules to produce an executable, regardless
 * of the license terms of these independent modules, and to copy and distribute
 * the resulting executable under terms of your choice, provided that you also meet,
 * for each linked independent module, the terms and conditions of the license of
 * that module. An independent module is a module which is not derived from or
 * based on this library. If you modify this library, you may extend this exception
 * to your version of the library, but you are not obligated to do so. If you do not
 * wish to do so, delete this exception statement from your version.
 *
 */
package org.geosdi.geoplatform;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.Assert;
import org.geosdi.geoplatform.core.model.GPFolder;
import org.geosdi.geoplatform.core.model.GPProject;
import org.geosdi.geoplatform.core.model.GPRasterLayer;
import org.geosdi.geoplatform.core.model.GPVectorLayer;
import org.geosdi.geoplatform.exception.IllegalParameterFault;
import org.geosdi.geoplatform.exception.ResourceNotFoundFault;
import org.geosdi.geoplatform.responce.FolderDTO;
import org.geosdi.geoplatform.responce.IElementDTO;
import org.geosdi.geoplatform.responce.ProjectDTO;
import org.geosdi.geoplatform.responce.RasterLayerDTO;
import org.geosdi.geoplatform.responce.VectorLayerDTO;
import org.geosdi.geoplatform.responce.collection.TreeFolderElements;
import org.junit.Test;

/**
 *
 * @author Vincenzo Monteverde
 * @email vincenzo.monteverde@geosdi.org - OpenPGP key ID 0xB25F4B38
 */
public class WSProjectTest extends ServiceTest {

    Map<String, Object> fixture = new HashMap<String, Object>();
    // Folder
    private final String nameFolder1A = "folder1A";
    private final String nameFolder1B = "folder1B";
    private final String nameFolder1C = "folder1C";
    private final String nameFolder2A = "folder2A";
    private final String nameFolder2B = "folder2B";
    private final String nameFolder2C = "folder2C";
    private final String nameFolder3A = "folder3A";
    private final String nameFolder3B = "folder3B";
    private final String nameFolder3C = "folder3C";
    private GPFolder folder1A;
    private GPFolder folder1B;
    private GPFolder folder1C;
    private GPFolder folder2A;
    private GPFolder folder2B;
    private GPFolder folder2C;
    private GPFolder folder3A;
    private GPFolder folder3B;
    private GPFolder folder3C;
    // Layer
    private String titleRaster = "T-raster-";
    private String nameRaster = "N-raster-";
    private String titleVector = "T-vector-";
    private String nameVector = "N-vector-";
    GPRasterLayer rasterRootFolderA;
    GPRasterLayer rasterFolder1B;
    GPRasterLayer rasterFolder2C;
    GPVectorLayer vectorFolder3A;
    GPVectorLayer vectorRootFolderB;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        Long idRasterRootFolderA = super.createAndInsertRasterLayer(super.rootFolderA, titleRaster + super.nameRootFolderA,
                nameRaster + super.nameRootFolderA, "", 15, "", "");
        rasterRootFolderA = gpWSClient.getRasterLayer(idRasterRootFolderA);
        this.fixture.put(rasterRootFolderA.getName(), rasterRootFolderA);
        // "rootFolderA" ---> "folder1(A|B|C)"
        Long idFolder1A = super.createAndInsertFolder(nameFolder1A, projectTest, 14, rootFolderA, 3);
        folder1A = gpWSClient.getFolderDetail(idFolder1A);
        this.fixture.put(folder1A.getName(), folder1A);
        //
        Long idFolder1B = super.createAndInsertFolder(nameFolder1B, projectTest, 13, rootFolderA, 1);
        folder1B = gpWSClient.getFolderDetail(idFolder1B);
        this.fixture.put(folder1B.getName(), folder1B);
        //
        Long idRasterFolder1B = super.createAndInsertRasterLayer(folder1B, titleRaster + nameFolder1B,
                nameRaster + nameFolder1B, "", 12, "", "");
        rasterFolder1B = gpWSClient.getRasterLayer(idRasterFolder1B);
        this.fixture.put(rasterFolder1B.getName(), rasterFolder1B);
        //
        Long idFolder1C = super.createAndInsertFolder(nameFolder1C, projectTest, 11, rootFolderA);
        folder1C = gpWSClient.getFolderDetail(idFolder1C);
        this.fixture.put(folder1C.getName(), folder1C);
        //
        // "folder1A" ---> "folder2(A|B|C)"
        Long idFolder2A = super.createAndInsertFolder(nameFolder2A, projectTest, 10, folder1A, 3);
        folder2A = gpWSClient.getFolderDetail(idFolder2A);
        this.fixture.put(folder2A.getName(), folder2A);
        //
        Long idFolder2B = super.createAndInsertFolder(nameFolder2B, projectTest, 9, folder1A);
        folder2B = gpWSClient.getFolderDetail(idFolder2B);
        this.fixture.put(folder2B.getName(), folder2B);
        //
        Long idFolder2C = super.createAndInsertFolder(nameFolder2C, projectTest, 8, folder1A, 1);
        folder2C = gpWSClient.getFolderDetail(idFolder2C);
        this.fixture.put(folder2C.getName(), folder2C);
        //
        Long idRasterFolder2C = super.createAndInsertRasterLayer(folder2C, titleRaster + nameFolder2C,
                nameRaster + nameFolder2C, "", 7, "", "");
        rasterFolder2C = gpWSClient.getRasterLayer(idRasterFolder2C);
        this.fixture.put(rasterFolder2C.getName(), rasterFolder2C);
        //
        // "folder2A" ---> "folder3(A|B|C)"
        Long idFolder3A = super.createAndInsertFolder(nameFolder3A, projectTest, 6, folder2A, 1);
        folder3A = gpWSClient.getFolderDetail(idFolder3A);
        this.fixture.put(folder3A.getName(), folder3A);
        //
        Long idVectorFolder3A = super.createAndInsertVectorLayer(folder3A, titleVector + nameFolder3A,
                nameVector + nameFolder3A, "", 5, "", "");
        vectorFolder3A = gpWSClient.getVectorLayer(idVectorFolder3A);
        this.fixture.put(vectorFolder3A.getName(), vectorFolder3A);
        //
        Long idFolder3B = super.createAndInsertFolder(nameFolder3B, projectTest, 4, folder2A);
        folder3B = gpWSClient.getFolderDetail(idFolder3B);
        this.fixture.put(folder3B.getName(), folder3B);
        //
        Long idFolder3C = super.createAndInsertFolder(nameFolder3C, projectTest, 3, folder2A);
        folder3C = gpWSClient.getFolderDetail(idFolder3C);
        this.fixture.put(folder3C.getName(), folder3C);
        //        
        super.rootFolderA.setPosition(16);
        super.rootFolderA.setNumberOfDescendants(13);
        gpWSClient.updateFolder(super.rootFolderA);


        Long idVectorRootFolderB = super.createAndInsertVectorLayer(super.rootFolderB, titleVector + super.nameRootFolderB,
                nameVector + super.nameRootFolderB, "", 2, "", "");
        vectorRootFolderB = gpWSClient.getVectorLayer(idVectorRootFolderB);
        this.fixture.put(vectorRootFolderB.getName(), vectorRootFolderB);
        //
        super.rootFolderB.setNumberOfDescendants(1);
        gpWSClient.updateFolder(super.rootFolderB);

        super.projectTest.setNumberOfElements(projectTest.getNumberOfElements() + 10);
        gpWSClient.updateProject(projectTest);
    }

    @Test
    public void testFixtureNotNull() {
        for (Map.Entry<String, Object> entry : fixture.entrySet()) {
            Assert.assertNotNull(entry.getKey() + " is NULL", entry.getValue());
        }
    }

    @Test
    public void testExportProject() throws ResourceNotFoundFault {
        ProjectDTO project = gpWSClient.exportProject(super.idProjectTest);

        Assert.assertEquals("project name", super.projectTest.getName(), project.getName());
        Assert.assertEquals("project elements", super.projectTest.getNumberOfElements(), project.getNumberOfElements().intValue());

        List<FolderDTO> rootFolders = project.getRootFolders();
        Assert.assertEquals("#root", 2, rootFolders.size());
        Assert.assertEquals("A", super.nameRootFolderA, rootFolders.get(0).getName());
        Assert.assertEquals("B", super.nameRootFolderB, rootFolders.get(1).getName());

        List<IElementDTO> childRootFolderA = rootFolders.get(0).getElementList();
        Assert.assertEquals("#A", 4, childRootFolderA.size());
        Assert.assertEquals("R-A", nameRaster + super.nameRootFolderA, childRootFolderA.get(0).getName());
        Assert.assertEquals("1A", nameFolder1A, childRootFolderA.get(1).getName());
        Assert.assertEquals("1B", nameFolder1B, childRootFolderA.get(2).getName());
        Assert.assertEquals("1C", nameFolder1C, childRootFolderA.get(3).getName());

        List<IElementDTO> childFolder1A = ((FolderDTO) childRootFolderA.get(1)).getElementList();
        Assert.assertEquals("#1A", 3, childFolder1A.size());
        Assert.assertEquals("2A", nameFolder2A, childFolder1A.get(0).getName());
        Assert.assertEquals("2B", nameFolder2B, childFolder1A.get(1).getName());
        Assert.assertEquals("2C", nameFolder2C, childFolder1A.get(2).getName());

        List<IElementDTO> childFolder2A = ((FolderDTO) childFolder1A.get(0)).getElementList();
        Assert.assertEquals("#2A", 3, childFolder2A.size());
        Assert.assertEquals("3A", nameFolder3A, childFolder2A.get(0).getName());
        Assert.assertEquals("3B", nameFolder3B, childFolder2A.get(1).getName());
        Assert.assertEquals("3C", nameFolder3C, childFolder2A.get(2).getName());

        FolderDTO f1B = (FolderDTO) childRootFolderA.get(2);
        Assert.assertEquals("#1B", 1, f1B.getElementList().size());
        Assert.assertEquals("R-1B", nameRaster + nameFolder1B, f1B.getElementList().get(0).getName());

        FolderDTO f2C = (FolderDTO) childFolder1A.get(2);
        Assert.assertEquals("#2C", 1, f2C.getElementList().size());
        Assert.assertEquals("R-2C", nameRaster + nameFolder2C, f2C.getElementList().get(0).getName());

        FolderDTO f3A = (FolderDTO) childFolder2A.get(0);
        Assert.assertEquals("#3A", 1, f3A.getElementList().size());
        Assert.assertEquals("V-3A", nameVector + nameFolder3A, f3A.getElementList().get(0).getName());

        List<IElementDTO> childRootFolderB = rootFolders.get(1).getElementList();
        Assert.assertEquals("#B", 1, childRootFolderB.size());
        Assert.assertEquals("V-B", nameVector + super.nameRootFolderB, childRootFolderB.get(0).getName());
    }

    @Test
    public void testOnlyFirstLevelFolder() throws ResourceNotFoundFault {
        gpWSClient.deleteFolder(super.idRootFolderA);

        ProjectDTO project = gpWSClient.exportProject(super.idProjectTest);
        Assert.assertEquals("project name", super.projectTest.getName(), project.getName());

        List<FolderDTO> rootFolders = project.getRootFolders();
        Assert.assertEquals("#root", 1, rootFolders.size());
        Assert.assertEquals("B", super.nameRootFolderB, rootFolders.get(0).getName());

        List<IElementDTO> childRootFolderB = rootFolders.get(0).getElementList();
        Assert.assertEquals("#B", 1, childRootFolderB.size());
        Assert.assertEquals("V-B", nameVector + super.nameRootFolderB, childRootFolderB.get(0).getName());
    }

    @Test
    public void testProjectDeep() {
        // TODO 
    }

    @Test
    public void testImportProject() throws IllegalParameterFault, ResourceNotFoundFault {
        // Create ProjectDTO to import
        ProjectDTO projectDTO = new ProjectDTO(super.projectTest);

        List<GPFolder> rootFolders = Arrays.asList(super.rootFolderA, super.rootFolderB);
        List<FolderDTO> rootFoldersDTO = FolderDTO.convertToFolderDTOList(rootFolders);
        projectDTO.setRootFolders(rootFoldersDTO);

        FolderDTO rootFolderADTO = rootFoldersDTO.get(0);
        rootFolderADTO.addLayer(new RasterLayerDTO(rasterRootFolderA));
        List<FolderDTO> childRootFolderA = FolderDTO.convertToFolderDTOList(Arrays.asList(folder1A, folder1B, folder1C));
        rootFolderADTO.addFolders(childRootFolderA);

        List<FolderDTO> childFolder1A = FolderDTO.convertToFolderDTOList(Arrays.asList(folder2A, folder2B, folder2C));
        childRootFolderA.get(0).addFolders(childFolder1A);
        childRootFolderA.get(1).addLayer(new RasterLayerDTO(rasterFolder1B));

        List<FolderDTO> childFolder2A = FolderDTO.convertToFolderDTOList(Arrays.asList(folder3A, folder3B, folder3C));
        childFolder1A.get(0).addFolders(childFolder2A);
        childFolder1A.get(2).addLayer(new RasterLayerDTO(rasterFolder2C));

        childFolder2A.get(0).addLayer(new VectorLayerDTO(vectorFolder3A));

        FolderDTO rootFolderBDTO = rootFoldersDTO.get(1);
        rootFolderBDTO.addLayer(new VectorLayerDTO(vectorRootFolderB));

        projectDTO.setId(null); // Entity passed must not containd an ID, otherwise Hibernate throws PersistentObjectException
        // Import ProjectDTO
        Long projectId = gpWSClient.importProject(projectDTO, super.idUserTest);
        // Check imported Project
        Assert.assertTrue("Check importProject", projectId > 0);
        logger.debug("*** ID project imported: {} ***", projectId);

        GPProject projectAdded = gpWSClient.getProjectDetail(projectId);
        Assert.assertEquals("project name", super.projectTest.getName(), projectAdded.getName());
        Assert.assertEquals("project elements", super.projectTest.getNumberOfElements(), projectAdded.getNumberOfElements());

        rootFoldersDTO = gpWSClient.getRootFoldersByProjectId(projectId);
        Assert.assertNotNull("rootFolders null", rootFoldersDTO);
        Assert.assertEquals("#root", 2, rootFoldersDTO.size());
        rootFolderADTO = rootFoldersDTO.get(0);
        rootFolderBDTO = rootFoldersDTO.get(1);
        Assert.assertEquals("A", super.nameRootFolderA, rootFolderADTO.getName());
        Assert.assertEquals("B", super.nameRootFolderB, rootFolderBDTO.getName());

        TreeFolderElements elemRootFolderA = gpWSClient.getChildrenElements(rootFolderADTO.getId());
        Assert.assertNotNull("elem-A null", elemRootFolderA);
        Assert.assertEquals("#A", 4, elemRootFolderA.size());
        Assert.assertEquals("R-A", nameRaster + super.nameRootFolderA, elemRootFolderA.pollFirst().getName());
        FolderDTO folder1ADTO = (FolderDTO) elemRootFolderA.pollFirst();
        Assert.assertEquals("1A", nameFolder1A, folder1ADTO.getName());
        FolderDTO folder1BDTO = (FolderDTO) elemRootFolderA.pollFirst();
        Assert.assertEquals("1B", nameFolder1B, folder1BDTO.getName());
        Assert.assertEquals("1C", nameFolder1C, elemRootFolderA.pollFirst().getName());

        TreeFolderElements elemFolder1A = gpWSClient.getChildrenElements(folder1ADTO.getId());
        Assert.assertNotNull("elem-1A null", elemFolder1A);
        Assert.assertEquals("#1A", 3, elemFolder1A.size());
        FolderDTO folder2ADTO = (FolderDTO) elemFolder1A.pollFirst();
        Assert.assertEquals("2A", nameFolder2A, folder2ADTO.getName());
        Assert.assertEquals("2B", nameFolder2B, elemFolder1A.pollFirst().getName());
        FolderDTO folder2CDTO = (FolderDTO) elemFolder1A.pollFirst();
        Assert.assertEquals("2C", nameFolder2C, folder2CDTO.getName());

        TreeFolderElements elemFolder2A = gpWSClient.getChildrenElements(folder2ADTO.getId());
        Assert.assertNotNull("elem-2A null", elemFolder2A);
        Assert.assertEquals("#2A", 3, elemFolder2A.size());
        FolderDTO folder3ADTO = (FolderDTO) elemFolder2A.pollFirst();
        Assert.assertEquals("3A", nameFolder3A, folder3ADTO.getName());
        Assert.assertEquals("3B", nameFolder3B, elemFolder2A.pollFirst().getName());
        Assert.assertEquals("3C", nameFolder3C, elemFolder2A.pollFirst().getName());

        TreeFolderElements elemFolder3A = gpWSClient.getChildrenElements(folder3ADTO.getId());
        Assert.assertNotNull("elem-3A null", elemFolder3A);
        Assert.assertEquals("#3A", 1, elemFolder3A.size());
        Assert.assertEquals("V-3A", nameVector + nameFolder3A, elemFolder3A.pollFirst().getName());

        TreeFolderElements elemFolder2C = gpWSClient.getChildrenElements(folder2CDTO.getId());
        Assert.assertNotNull("elem-2C null", elemFolder2C);
        Assert.assertEquals("#2C", 1, elemFolder2C.size());
        Assert.assertEquals("R-2C", nameRaster + nameFolder2C, elemFolder2C.pollFirst().getName());

        TreeFolderElements elemFolder1B = gpWSClient.getChildrenElements(folder1BDTO.getId());
        Assert.assertNotNull("elem-1B null", elemFolder1B);
        Assert.assertEquals("#1B", 1, elemFolder1B.size());
        Assert.assertEquals("R-1B", nameRaster + nameFolder1B, elemFolder1B.pollFirst().getName());

        TreeFolderElements elemRootFolderB = gpWSClient.getChildrenElements(rootFolderBDTO.getId());
        Assert.assertNotNull("elem-B null", elemRootFolderB);
        Assert.assertEquals("#B", 1, elemRootFolderB.size());
        Assert.assertEquals("V-B", nameVector + super.nameRootFolderB, elemRootFolderB.pollFirst().getName());
    }
}