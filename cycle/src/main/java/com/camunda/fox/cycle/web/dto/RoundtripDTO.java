package com.camunda.fox.cycle.web.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.camunda.fox.cycle.entity.BpmnDiagram;
import com.camunda.fox.cycle.entity.Roundtrip;

/**
 * This is a data object which exposes a {@link Roundtrip} to the client via rest.
 * 
 * It is needed to ensure clear boundaries between a JPA managed entity (the {@link Roundtrip}) 
 * and the json serialization mechanism. 
 * 
 * @author nico.rehwaldt
 */
public class RoundtripDTO {

  private Long id;
  
  private String name;
  
  private BpmnDiagramDTO leftHandSide;
  
  private BpmnDiagramDTO rightHandSide;
  
  private Date lastSync;
  
  public RoundtripDTO() { }
  
  public RoundtripDTO(Roundtrip r) {
    this.id = r.getId();
    this.name = r.getName();
    this.lastSync = r.getLastSync();
  }
  
  public RoundtripDTO(Roundtrip r, BpmnDiagram leftHandSide, BpmnDiagram rightHandSide) {
    this(r);
    
    this.leftHandSide = leftHandSide != null ? BpmnDiagramDTO.wrap(leftHandSide) : null;
    this.rightHandSide = rightHandSide != null ? BpmnDiagramDTO.wrap(rightHandSide) : null;
  }
  
  /**
   * @return the id
   */
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  
  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public Date getLastSync() {
    return lastSync;
  }
  
  public BpmnDiagramDTO getLeftHandSide() {
    return leftHandSide;
  }
  
  public void setLeftHandSide(BpmnDiagramDTO leftHandSide) {
    this.leftHandSide = leftHandSide;
  }

  public BpmnDiagramDTO getRightHandSide() {
    return rightHandSide;
  }

  public void setRightHandSide(BpmnDiagramDTO rightHandSide) {
    this.rightHandSide = rightHandSide;
  }
  
  /**
   * Wraps a roundtrip as a data object
   * @param roundtrip
   * @return 
   */
  public static RoundtripDTO wrap(Roundtrip roundtrip) {
    return new RoundtripDTO(roundtrip);
  }
  
  /**
   * Wraps a list of roundtrips as a list of the respective roundtrip data objects
   * 
   * @param trackers
   * @return 
   */
  public static List<RoundtripDTO> wrapAll(List<Roundtrip> trackers) {
    List<RoundtripDTO> dtos = new ArrayList<RoundtripDTO>();
    for (Roundtrip t: trackers) {
      dtos.add(new RoundtripDTO(t));
    }
    
    return dtos;
  }
}
