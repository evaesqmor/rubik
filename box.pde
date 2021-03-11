class Box {
  PMatrix3D matrix; //Matriz de posiciones del cubo
  int x, y , z = 0;
  boolean highlight;
  color c;
  Face[] faces = new Face[6];
  
  // Constructor
  Box(PMatrix3D m,  int x , int y, int z){
    this.matrix = m;
    this.x = x;
    this.y = y;
    this.z = z;
    c = color(255);
    //Caras del cubo
    faces[0] = new Face(new PVector(0, 0, -1), color(0, 0, 255));
    faces[1] = new Face(new PVector(0, 0, 1), color(0, 255, 0));
    faces[2] = new Face(new PVector(0, 1, 0), color(255, 255, 255));
    faces[3] = new Face(new PVector(0, -1, 0), color(255, 255, 0));
    faces[4] = new Face(new PVector(1, 0, 0), color(255, 150, 0));
    faces[5] = new Face(new PVector(-1, 0, 0), color(255, 0, 0));
  }
  
  // Girar las caras
  void turnFacesZ(int dir){
    for(Face f: faces){
      f.turnZ(dir * HALF_PI);
    }
  }
  
  void turnFacesY(int dir) {
    for (Face f : faces) {
      f.turnY(dir * HALF_PI); 
    }
  }
  
  void turnFacesX(int dir) {
    for (Face f : faces) {
      f.turnX(dir * HALF_PI); 
    }
  }
  
  // Actualizando los valores de x,y,z
  void update(int x, int y, int z){
   matrix.reset();
   matrix.translate(x,y,z);
   this.x = x;
   this.y = y;
   this.z = z; 
  }
  
  // Función para mostrar el cubo
  void show(){
    //fill(c);
    noFill();
    stroke(0);
    strokeWeight(0.1);
    pushMatrix(); // Guardamos la información del estado del cubo
    applyMatrix(matrix); // Cada pieza tiene una matriz
    box(1);
    for(Face f:faces){
      f.show();
    }
    popMatrix(); //Recuperamos la información
   }
  
  

}
