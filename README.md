# cubes-puzzle
Efficient solver for http://www.happycube.com/

## Usage example
```java
List<Face> faces = new ArrayList<>();
faces.add(new Face(new byte[] {1,1,0,1,0,0,0,1,0,0,1,0,0,0,1,1}, 5));
faces.add(new Face(new byte[] {0,0,0,1,1,0,1,0,0,1,0,1,0,0,1,1}, 5));
faces.add(new Face(new byte[] {0,1,0,0,0,0,1,0,0,0,1,0,0,1,0,1}, 5));
faces.add(new Face(new byte[] {0,1,0,1,1,0,1,0,0,1,0,1,1,1,0,0}, 5));
faces.add(new Face(new byte[] {0,0,1,0,1,1,1,0,0,1,1,0,1,1,1,0}, 5));
faces.add(new Face(new byte[] {1,1,0,1,1,1,0,0,0,1,0,1,0,0,1,0}, 5));

try (ConfigurationWriter writer = ConfigurationWriter.fileWriter(file)) {

  CubeVisitor visitor = new CubeVisitor() {
    @Override
    public void visit(Cube cube) {
      writer.writeConfiguration(Configuration.fromCube(cube));
    }
  };
  
  Solver.happyCube(faces).cubeVisitor(visitor).uniqueSolutions().solve();

} catch (IOException) {
  ...
}
```

### Output
```
Initial faces (edge size = 5):

   XX X       XX    X   
   XXXX    XXXX    XXXX 
   XXXX    XXXXX    XXXX
    XXXX    XXX    XXXX 
     X      X X      X  

    X XX     X X   XX XX
    XXX     XXXX    XXXX
    XXXX   XXXXX   XXXX 
   XXXX    XXXX     XXX 
   XX X    X XX     X X 

************************
Solution #1

            X XX        
            XXXX        
           XXXX         
            XXX         
           XX X         

    X X      X     XXX  
    XXXX    XXXX    XXX 
   XXXX    XXXX    XXXXX
    XXX    XXXX    XXXX 
     X     XX X      XXX

             X X        
           XXXXX        
            XXX         
           XXXX         
             XX         

           XX           
           XXXXX        
            XXX         
           XXXXX        
           X X          


************************
Solution #2

           XX X         
           XXXX         
            XXXX        
            XXX         
            X XX        

     XXX     X      X X 
    XXX    XXXX    XXXX 
   XXXXX    XXXX    XXXX
    XXXX    XXXX    XXX 
   XXX      X XX     X  

           X X          
           XXXXX        
            XXX         
            XXXX        
            XX          

              XX        
           XXXXX        
            XXX         
           XXXXX        
             X X        
```

### Elimination of symmetric (isomorphic) solutions
Currently the following types of symmetry are eliminated:
- orientation of the cube in space
- horizontal rotations (migration of 4 sides in north -> east -> south -> west order with rotation of upper and bottom sides)

Symmetry of sides' flips is not done yet.

In case you have some interest in reading source, the following picture might help:
![cube-geometry](https://github.com/atomashpolskiy/atomashpolskiy.github.io/blob/master/static/img/cube.png)
