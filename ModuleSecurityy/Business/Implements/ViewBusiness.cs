using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using static System.Runtime.InteropServices.JavaScript.JSType;

namespace Business.Implements
{
    public class ViewBusiness : IViewBusiness
    {
        protected readonly IViewData data;
    }
    public ViewBusinessIViewData data) 
    {
        this.data = data;
    }
    public async Task Delete(int id)
    {
        await this.data.Delete(id);
    }

    public async Task<IEnumerable<ViewDto>> GetAll()
    {
        IEnumerable<View> views = await this.data.GetAll();
        var viewDtos = views.Select view => new ViewDto
        {
            Id = view.Id,
            Name = view.Name,
            Description = view.Description,
            Route = view.Route,
            Modulold = view.Modulold,
            State = view.State
        });
        return viewDtos;
    }

    public async Task<IEnumerable<DataSelectDto>> GetAllSelect()
    {
        return await this.data.GetAllSelect();
    }
    public async Task<ViewDto> GetById(int id)
    {
        View view = await this.data.GetById(id);
        ViewDto viewDto = new ViewDto();

        viewDto.Id = view.Id;
        viewDto.Name = view.Name;
        viewDto.Description = view.Description;
        viewDto.Route = view.Route;
        viewDto.Modulold = view.Modulold;
        viewDto.State = view.State;
        return viewDto;
    }

    public View mapearDatos(View view, ViewDto entity)
    {
        view.Id = entity.Id;
        view.Name = entity.Name;
        view.Description = entity.Description;
        view.Route = entity.Route;
        view.Modulold = entity.Moduloid;
        view.State = entity.State;
        return view;
    }

    public async Task<View>Save(ViewDto entity)
    {
        View view = new View();
        view.Created_at = DateTime.Now.AddHours(-5);
        view = this.mapearDatos(view, entity);
        view.Modulo = null;

        return await this.data.Save(view);
    }

    public async Task Update(ViewDto entity)
    {
        View view = await this.data.GetById(entity.Id);
        if (view == null)
        {
            throw new Exception("Registro no encontrado");
        }
        view = this.mapearDatos(view, entity);

        await this.data.Update(view);
    }
}





















