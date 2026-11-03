const r=require("express").Router(), mongoose=require("mongoose");
const Patient=require("../models/Patient"),Doctor=require("../models/Doctor"),Appointment=require("../models/Appointment");
const models={patients:Patient,doctors:Doctor,appointments:Appointment};
for(const [path,M] of Object.entries(models)){
 r.post("/"+path,async(req,res,next)=>{try{res.status(201).json(await M.create(req.body));}catch(e){next(e);}});
 r.get("/"+path,async(req,res,next)=>{try{let filter={};if(path==="doctors"&&req.query.search)filter.specialization=new RegExp(req.query.search,"i");if(path==="appointments"&&req.query.status)filter.status=req.query.status;let page=Math.max(1,+req.query.page||1),limit=Math.min(100,Math.max(1,+req.query.limit||10));let q=M.find(filter);if(path==="appointments")q=q.populate("patient doctor");let [data,total]=await Promise.all([q.sort({createdAt:-1}).skip((page-1)*limit).limit(limit),M.countDocuments(filter)]);res.json({data,pagination:{page,limit,total}});}catch(e){next(e);}});
 r.get("/"+path+"/:id",async(req,res,next)=>{try{if(!mongoose.isValidObjectId(req.params.id))return res.status(400).json({message:"Invalid ID"});let q=M.findById(req.params.id);if(path==="appointments")q=q.populate("patient doctor");let d=await q;if(!d)return res.status(404).json({message:"Not found"});res.json(d);}catch(e){next(e);}});
 r.patch("/"+path+"/:id",async(req,res,next)=>{try{if(!mongoose.isValidObjectId(req.params.id))return res.status(400).json({message:"Invalid ID"});let d=await M.findByIdAndUpdate(req.params.id,req.body,{new:true,runValidators:true});if(!d)return res.status(404).json({message:"Not found"});res.json(d);}catch(e){next(e);}});
 r.delete("/"+path+"/:id",async(req,res,next)=>{try{let d=await M.findByIdAndDelete(req.params.id);if(!d)return res.status(404).json({message:"Not found"});res.json({message:"Deleted"});}catch(e){next(e);}});
}
r.get("/reports/appointments-by-status",async(req,res,next)=>{try{res.json(await Appointment.aggregate([{$group:{_id:"$status",count:{$sum:1}}}]));}catch(e){next(e);}});
module.exports=r;