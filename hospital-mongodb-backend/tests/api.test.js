const mongoose=require("mongoose"),{MongoMemoryServer}=require("mongodb-memory-server"),request=require("supertest"),app=require("../src/app");let mongo;
beforeAll(async()=>{mongo=await MongoMemoryServer.create();await mongoose.connect(mongo.getUri());});
afterAll(async()=>{await mongoose.disconnect();await mongo.stop();});
afterEach(async()=>{for(const c of Object.values(mongoose.connection.collections))await c.deleteMany({});});
test("health check",async()=>{expect((await request(app).get("/health")).body.status).toBe("ok");});
test("patient create and read",async()=>{let p={name:"Riya Mehta",email:"riya@example.com",phone:"9876543210",dateOfBirth:"2002-04-12",gender:"female",bloodGroup:"B+"};let c=await request(app).post("/api/patients").send(p);expect(c.status).toBe(201);let g=await request(app).get("/api/patients/"+c.body._id);expect(g.body.email).toBe(p.email);});