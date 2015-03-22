Generates environment specific application configurations for multiple environments (examples here for local and prod).
    
The resources in this parent src/main/resources act as a template and apply to each environment.  
If the resource-file is optional or very environment specific, for example logging config, then put it in {{<env>/src/main/resources}}
    
This build makes the environment-specific artifacts are available as a .zip file. 
