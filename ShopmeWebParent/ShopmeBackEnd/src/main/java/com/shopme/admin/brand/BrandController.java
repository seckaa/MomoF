package com.shopme.admin.brand;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.AmazonS3Util;
import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.category.CategoryService;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.user.UserService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@Controller
public class BrandController {
	private String defaultRedirectURL = "redirect:/brands/page/1?sortField=name&sortDir=asc";
	@Autowired
	private BrandService brandService;
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/brands")
	public String listFirstPage(){
//		return "redirect:/brands/page/1?sortField=name&sortDir=asc";
		return defaultRedirectURL;
	}
	
	@GetMapping("/brands/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listBrands", moduleURL = "/brands") PagingAndSortingHelper helper,
			@PathVariable(name ="pageNum") int pageNum, Model model
			) {
		
		brandService.listByPage(pageNum, helper);
		return "brands/brands";
	}
	
	
	@GetMapping("/brands/new")
	public String newCategory(Model model) {
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("pageTitle", "Create New Brand");
		model.addAttribute("brand", new Brand());
		
		return "brands/brand_form";
	}
	
	@PostMapping("/brands/save")
	public String saveBrand(Brand brand,
			@RequestParam("fileImage") MultipartFile multipartFile,
			RedirectAttributes ra) throws IOException {
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			brand.setLogo(fileName);
		
		Brand savedBrand = brandService.save(brand);
		String uploadDir = "brand-logos/" + savedBrand.getId();
		
//		FileUploadUtil.cleanDir(uploadDir);
//		FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		
		AmazonS3Util.removeFolder(uploadDir);
		AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
		
		}else {
			brandService.save(brand);
		}
		ra.addFlashAttribute("message", "The brand has been saved successfully");
//		return "redirect:/brands";
		return defaultRedirectURL;		
	}
	
	@GetMapping("/brands/edit/{id}")
	public String editBrand(@PathVariable(name="id") Integer id, Model model,
			RedirectAttributes ra){	
		try {
			Brand brand =  brandService.get(id);
			List<Category> listCategories = categoryService.listCategoriesUsedInForm();
			
			model.addAttribute("brand",brand );
			model.addAttribute("listCategories", listCategories);
			model.addAttribute("pageTitle", "Edit Brand (ID:	" +id+ ")");
			
			return "brands/brand_form";
		}catch(BrandNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
//			return "redirect:/brands";
			return defaultRedirectURL;
		}	
	}
	@GetMapping("/brands/delete/{id}")
	public String deleteBrand(@PathVariable(name = "id") Integer id, 
			Model model,
			RedirectAttributes redirectAttributes) {
		
		try {
			brandService.delete(id);
			String brandDir = "brand-logos/"+id;
//			FileUploadUtil.removeDir(brandDir);
			AmazonS3Util.removeFolder(brandDir);
			
			redirectAttributes.addFlashAttribute("message",
					"The brand with id : "+id+" has been deleted successfully");
			
			}catch(BrandNotFoundException ex){
				redirectAttributes.addFlashAttribute("message", ex.getMessage());
			}
//		return "redirect:/brands";
		return defaultRedirectURL;
	}
}
