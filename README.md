This is a project of course "Análisis Estatico de Programas 2013" at UNRC. You will found here a source code
 writen by Laura Tardivo and Gastón Scilingo. 

Run Configuration

To run the parser you must provide the following arguments:
   arg 1) The source code of the program to be parsed, 
   arg 2) The type of analysis:

       CFG  = Control Flow Graph
       DT   = Dominators Tree
       PDT  = Post-Dominators Tree
       IPDT = Inmediate Post-Dominators Tree
       CDG  = Control Dependence Graph
       DDG  = Data Dependence Graph
       SG   = Slicing Graph

   arg 3) (For slicing - option SG of arg 3) Number of node in the CFG from which to compute the slice of the program.
   arg 4) List of flags:
       -mac = indicates the native operating system
       -debbug = debbug mode. Will write to stdout some debbug information.

The execution will open a jpg file in your default image viewer, describing the output graph or tree computed.

Example 1: Compute the dominators tree (MAC OS), non-debbug mode

   /home/static-analysis$ java -jar static-analysis.jar my_code.txt DT -mac

Example 2: Compute slicing from node 4 of CFG (Linux OS - debbug mode)
                                    
   /home/static-analysis$ java -jar static-analysis.jar my_code.txt SG 4 -debbug 
