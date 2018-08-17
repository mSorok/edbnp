package de.unijena.cheminf.edbnp.controllers;



import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.stream.Collectors;
import java.io.File;

import org.apache.commons.math3.util.Pair;
import de.unijena.cheminf.edbnp.readers.ReadWorker;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.validation.BindingResult;


import de.unijena.cheminf.edbnp.storage.StorageFileNotFoundException;
import de.unijena.cheminf.edbnp.storage.StorageService;




@Controller
public class FileUploadController {


    private final StorageService storageService;
    private boolean returnedResults = false;

    public ArrayList<String> readMolecules = new ArrayList<String>();
    public Hashtable<String, Pair>  fileStats  = new Hashtable<String, Pair>();


    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));



        return "index";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }




    @PostMapping("/")
    public String launchMoleculeReaderOnFile(@RequestParam("file") MultipartFile file,
                                       RedirectAttributes redirectAttributes) {

        if(!file.isEmpty()) {
            storageService.store(file);


            String loadedFile = "raw_data/" + file.getOriginalFilename();

            ReadWorker worker = new ReadWorker(loadedFile);

            boolean workerStarted = worker.startWorker();

            if (workerStarted) {

                redirectAttributes.addFlashAttribute("workerStarted",
                        "Reading Molecules");


                System.out.println("I'm here!!!! ");

                this.fileStats.put(file.getOriginalFilename() , worker.getFileStats() );




                return "redirect:/results";//redirect to results page when finished
            } else {
                storageService.deleteFile(file);
                redirectAttributes.addFlashAttribute("badFileType",
                        "Bad file type! Accepted formats: SDF, MOL & SMI (SMILES)");
                return "redirect:/";
            }

        }else {

            redirectAttributes.addFlashAttribute("noFileError",
                    "You need to load a file!");


            return "redirect:/";

        }



    }


    @GetMapping("results")
    public String listMolecules(Model model) throws IOException {


        //model.addAttribute("molecules", this.readMolecules);

        model.addAttribute("files_stats", this.fileStats);


        //model.addAttribute("number_submitted", "Number of molecules in submitted file: "+this.fileStats.getFirst());
        //model.addAttribute("number_loaded", "Number of valid molecules loaded: "+this.fileStats.getSecond());

        System.out.println(this.readMolecules);

        return "results";
    }


    @GetMapping("workonmolecules")
    public String workOnMolecules(Model model) throws IOException{

        System.out.println("STARTING WORKING ON MOLECULES");
        // for each molecule
        //


        return "workonmolecules";
    }

    /*@GetMapping("/wait")
    public String letsWait(RedirectAttributes redirectAttributes){

        System.out.println("In wait 1");

       System.out.println("\n\n WAITING \n\n\n");
       // redirectAttributes.addFlashAttribute("wait",
         //       "Waiting for the results!");

       //DO STUFF HERE
        System.out.println("In wait");

       return "redirect:/results";
    }
*/
   /* @RequestMapping("/results")
    @ResponseBody
    public String postResults(RedirectAttributes redirectAttributes){
            System.out.println("\n\n Results \n\n\n");
            redirectAttributes.addFlashAttribute("result",
                    "Here will be results");
            System.out.println("In results");
            return "redirect:/results";

    }

*/

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    //Cast multipart spring file to conventional file
    public File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException
    {
        File convFile = new File( multipart.getOriginalFilename());
        multipart.transferTo(convFile);
        return convFile;
    }
}
