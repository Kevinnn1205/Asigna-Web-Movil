using Microsoft.AspNetCore.Components;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.AspNetCore.Mvc;

namespace Web.Controllers.Implements
{
    [ApiController]
    [Route("[controller]")]
    public class ModuloController : ControllerBase
    {
        private readonly IModuloBusiness_moduloBusiness;
        {
            _moduloBusiness = moduloBusiness;
        }
    [HttpGet]
    public async Task<ActionResult<IEnumerable<ModuloDto>>> GetAll()
    {
        var result = await _moduloBusiness.GetAll();
        return Ok(result);
    }

    [HttpGet("{id}"]
    public async Task<ActionResult<ModuloDto>>GetByid(int id)
    {
        var result = await _moduloBusiness.GetByid(id);
        if (result == null)
        {
            return NotFound();
        }
        return Ok(result);
    }

    }

