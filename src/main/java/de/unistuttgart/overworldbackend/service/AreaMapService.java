package de.unistuttgart.overworldbackend.service;

import aj.org.objectweb.asm.TypeReference;
import de.unistuttgart.overworldbackend.data.*;
import de.unistuttgart.overworldbackend.data.config.AreaConfig;
import de.unistuttgart.overworldbackend.data.mapper.AreaMapMapper;
import de.unistuttgart.overworldbackend.data.mapper.CustomAreaMapMapper;
import de.unistuttgart.overworldbackend.repositories.AreaMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class AreaMapService {

    @Autowired
    private AreaMapRepository areaMapRepository;

    @Autowired
    private AreaService areaService;

    @Autowired
    private WorldService worldService;

    @Autowired
    private DungeonService dungeonService;

    @Autowired
    private AreaMapMapper areaMapMapper;

    @Autowired
    private CustomAreaMapMapper customAreaMapMapper;

    /**
     * Get the AreaMap from a world by its index and a course by its id
     * @param courseId the id of the course
     * @param worldIndex the index of the world
     * @return the found AreaMap
     */
    public AreaMap getAreaMapFromWorld(final int courseId, final int worldIndex)
    {
        return worldService
                .getWorldByIndexFromCourse(courseId, worldIndex)
                .getAreaMap();
    }

    /**
     * Get the AreaMap from a dungeon by its index, the world its in by its index and a course by its id
     * @param courseId the id of the course
     * @param worldIndex the index of the world
     * @param dungeonIndex the index of the dungeon
     * @return the found AreaMap
     */
    public AreaMap getAreaMapFromDungeon(final int courseId, final int worldIndex, final int dungeonIndex)
    {
        return dungeonService
                .getDungeonByIndexFromCourse(courseId, worldIndex, dungeonIndex)
                .getAreaMap();
    }

    /**
     * Get the AreaMaps from all areas of a course by its id
     * @param courseId the id of the course
     * @return Set of AreaMaps of the course
     */
    public Set<AreaMap> getAllAreaMapsFromCourse(final int courseId)
    {
        Set<AreaMap> areaMaps = new HashSet<>();
        Set<World> worlds = worldService.getAllWorldsFromCourse(courseId);
        for (World world: worlds) {
            areaMaps.add(world.getAreaMap());
            List<Dungeon> dungeons = world.getDungeons();
            for (Dungeon dungeon: dungeons) {
                areaMaps.add(dungeon.getAreaMap());
            }
        }
        return areaMaps;
    }

    /**
     * Update an area map of a world by its id from a course
     * @param courseId the id of the course the world is part of
     * @param worldIndex the index of the world
     * @param areaMapDTO the updated area map
     * @return the updated area map as DTO
     */
    public AreaMapDTO updateAreaMapOfWorld(final int courseId, final int worldIndex, final AreaMapDTO areaMapDTO){
        worldService.updateWorldContent(courseId, worldIndex, areaMapDTO);
        final AreaMap areaMap = getAreaMapFromWorld(courseId, worldIndex);
        return updateAreaMap(areaMapDTO, areaMap);
    }

    /**
     * Update an area map of a dungeon by its id from a course
     * @param courseId the id of the course the dungeon is part of
     * @param worldIndex the index of the world
     * @param dungeonIndex the index of the dungeon
     * @param areaMapDTO the updated area map
     * @return the updated area map as DTO
     */
    public AreaMapDTO updateAreaMapOfDungeon(final int courseId, final int worldIndex, final int dungeonIndex, final AreaMapDTO areaMapDTO){
        dungeonService.updateDungeonContent(courseId, worldIndex, dungeonIndex, areaMapDTO);
        final AreaMap areaMap = getAreaMapFromDungeon(courseId, worldIndex, dungeonIndex);
        return updateAreaMap(areaMapDTO, areaMap);
    }

    /**
     * Updates an area map with a given area map DTO
     * @param areaMapDTO the updated area map
     * @param areaMap the area map to be updated
     * @return the updated area map as DTO
     */
    private AreaMapDTO updateAreaMap(AreaMapDTO areaMapDTO, AreaMap areaMap) {
        areaMap.setGeneratedArea(areaMapDTO.isGeneratedArea());
        if(areaMap.isGeneratedArea())
        {
            CustomAreaMapDTO customAreaMapDTO = areaMapDTO.getAreaMapDTO();
            CustomAreaMap customAreaMap = customAreaMapMapper.customAreaMapDTOToCustomAreaMap(customAreaMapDTO);
            areaMap.setCustomAreaMap(customAreaMap);
        }
        else
        {
            areaMap.setCustomAreaMap(null);
        }
        final AreaMap updatedAreaMap = areaMapRepository.save(areaMap);
        return areaMapMapper.areaMapToAreaMapDTO(updatedAreaMap);
    }
}
