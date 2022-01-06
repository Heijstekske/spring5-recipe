package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class RecipeControllerTest {

   @Mock
   RecipeService recipeService;

   RecipeController controller;
   MockMvc mockMvc;

   @Before
   public void setup() throws Exception{
      MockitoAnnotations.initMocks(this);

      controller = new RecipeController(recipeService);
      mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
   }

   @Test
   public void testGetRecipe() throws Exception{
      Recipe recipe = new Recipe();
      recipe.setId(1L);

      when(recipeService.findById(anyLong())).thenReturn(recipe);

      mockMvc.perform(MockMvcRequestBuilders.get("/recipe/1/show/"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("recipe/show"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("recipe"));
   }

   @Test
   public void testGetNewRecipeForm() throws Exception{

      mockMvc.perform(MockMvcRequestBuilders.get("/recipe/new/"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("recipe/recipeform"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("recipe"));
   }

   @Test
   public void testPostNewRecipeForm() throws Exception {
      RecipeCommand command = new RecipeCommand();
      command.setId(1L);

      when(recipeService.saveRecipeCommand(any())).thenReturn(command);

      mockMvc.perform(MockMvcRequestBuilders.post("/recipe/")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("id", "")
            .param("description", "some string"))

            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.view().name("redirect:/recipe/1/show"));
   }

   @Test
   public void getUpdateView() throws Exception{
      RecipeCommand command = new RecipeCommand();
      command.setId(1L);

      when(recipeService.findCommandById(anyLong())).thenReturn(command);

      mockMvc.perform(MockMvcRequestBuilders.get("/recipe/1/update"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("recipe/recipeform"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("recipe"));
   }
}
