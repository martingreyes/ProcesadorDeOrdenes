import dayjs from 'dayjs';

export interface IOrden {
  id?: number;
  cliente?: number | null;
  accionId?: number | null;
  accion?: string | null;
  operacion?: string | null;
  precio?: number | null;
  cantidad?: number | null;
  fechaOperacion?: string | null;
  modo?: string | null;
  analisis?: boolean | null;
  procesamiento?: boolean | null;
  descripcion?: string | null;
}

export const defaultValue: Readonly<IOrden> = {
  analisis: false,
  procesamiento: false,
};
