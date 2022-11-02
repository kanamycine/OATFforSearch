package com.team6.onandthefarm.entity.exhibition;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Builder
@Slf4j
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ModuleImg {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long moduleImgId;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "moduleId")
	private Module module;

	private String moduleImgSrc;

	@Builder
	public ModuleImg(String moduleImgSrc, Module module){
		this.module = module;
		this.moduleImgSrc = moduleImgSrc;
	}

	public void updateModuleImg(String moduleImgSrc) {
		this.moduleImgSrc = moduleImgSrc;
	}

}
